package com.xirtam.data;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.xirtam.common.NString;
import com.xirtam.ui.XWidget;
import com.xirtam.ui.widget.XLayout;
import com.xirtam.utils.XNodeFactory;
import com.xirtam.utils.UIUtlis;

public class AnimatorTree extends AbstractComponentDecorator implements
		TreeExpansionListener {
	// private static final boolean SPRING = false;
	// private static final int INTERVAL = 41;
	private static Timer timer = new Timer(true);
	private Counter counter;
	private int insertionRow = -1;
	private TreePath draggedPath;
	private Set<TreePath> springs = new HashSet<TreePath>();
	private JTree tree;
	private Map<TreePath, Rectangle> bounds = new HashMap<TreePath, Rectangle>();
	private GhostedDragImage dragImage;
	private Point origin;
	private boolean dragActive;
	private XLayout bufParent;

	public AnimatorTree(JTree tree) {
		super(tree);
		this.tree = tree;
	}

	protected boolean canMove(TreePath path) {
		Object o = path.getLastPathComponent();
		return !o.equals(this.tree.getModel().getRoot());
	}

	protected boolean canMove(TreePath fromPath, TreePath toPath, int index) {
		if (canMove(fromPath)) {
			TreePath testPath = toPath;
			while (testPath != null) {
				if (testPath.equals(fromPath))
					return false;
				testPath = testPath.getParentPath();
			}
			return true;
		}
		return false;
	}

	protected void move(TreePath fromPath, TreePath toPath, int index) {
		Object moved = fromPath.getLastPathComponent();
		Object movedTo = toPath.getLastPathComponent();
		if (((this.tree.getModel() instanceof DefaultTreeModel))
				&& ((moved instanceof MutableTreeNode))
				&& ((movedTo instanceof MutableTreeNode))) {
			DefaultTreeModel treeModel = (DefaultTreeModel) this.tree
					.getModel();
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) moved;
			DefaultMutableTreeNode newParent = (DefaultMutableTreeNode) movedTo;
			XWidget c = (XWidget) child.getUserObject();
			XWidget p = (XWidget) newParent.getUserObject();
			treeModel.removeNodeFromParent(child);
			treeModel.insertNodeInto(child, newParent, index);
			XNodeFactory.addInto(c, p);
			c.packParent();
			if (bufParent != null)
				bufParent.pack();
			XNodeFactory.sortNodes();// 重排层顺序
			UIUtlis.refreshAllJsonNodeData();
		} else {
			throw new UnsupportedOperationException("You must override move()");
		}
	}

	public boolean startDrag(Point where) {
		this.draggedPath = this.tree.getPathForLocation(where.x, where.y);
		XWidget a = null;
		if (draggedPath != null)
			a = ((XWidget) ((DefaultMutableTreeNode) draggedPath
					.getLastPathComponent()).getUserObject())
					.getParentCPWidget();
		if (a != null && NString.T_LAYOUT.equals(a.getType()))
			bufParent = (XLayout) a;
		if ((this.draggedPath != null) && (canMove(this.draggedPath))) {
			this.dragActive = true;
			this.tree.collapsePath(this.draggedPath);
			this.origin = where;
			this.insertionRow = this.tree.getRowForPath(this.draggedPath);
			this.dragImage = new GhostedDragImage(this.draggedPath, this.origin);
			return true;
		}
		return false;
	}

	@Override
	public void treeExpanded(TreeExpansionEvent e) {
		int oldRowCount = this.bounds.size();
		int rows = this.tree.getRowCount();
		int start = this.tree.getRowForPath(e.getPath()) + 1;
		Rectangle rect = this.tree.getPathBounds(e.getPath());
		for (int i = 0; i < rows - oldRowCount; i++) {
			TreePath path = this.tree.getPathForRow(start + i);
			this.bounds.put(path, new Rectangle(rect));
		}
		repaint();
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent e) {
		for (Iterator<TreePath> i = this.bounds.keySet().iterator(); i
				.hasNext();) {
			TreePath path = (TreePath) i.next();
			if (this.tree.getRowForPath(path) == -1) {
				i.remove();
			}
		}
		repaint();
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			this.tree.addTreeExpansionListener(this);
			int size = this.tree.getRowCount();
			for (int i = 0; i < size; i++) {
				TreePath path = this.tree.getPathForRow(i);
				this.bounds.put(path, getProjectedPathBounds(path));
			}
			this.counter = new Counter();
			timer.schedule(this.counter, 41L, 41L);
		} else {
			// this.tree.removeTreeExpansionListener(this);
			// this.bounds.clear();
			// if (this.counter != null) {
			// this.counter.cancel();
			// this.counter = null;
			// }
		}
	}

	public void endDrag(Point where) {
		if (!this.dragActive)
			throw new IllegalStateException("Not dragging");
		TargetLocation loc = getTargetLocation(where);
		int draggedRow = this.tree.getRowForPath(this.draggedPath);
		this.dragImage.dispose();
		this.dragImage = null;
		this.insertionRow = -1;
		if ((loc != null) && (loc.insertionRow != -1)
				&& (loc.insertionRow != draggedRow)) {
			move(this.draggedPath, loc.parentPath, loc.index);
			this.bounds.remove(this.draggedPath);
		}
		this.draggedPath = null;
		this.dragActive = false;
	}

	public void dispose() {
		this.tree.removeTreeExpansionListener(this);
		super.dispose();
	}

	private boolean reposition() {
		boolean changed = false;
		for (Iterator<TreePath> i = this.bounds.keySet().iterator(); i
				.hasNext();) {
			TreePath path = (TreePath) i.next();
			Rectangle current = (Rectangle) this.bounds.get(path);
			if (current == null) {
				// System.err.println("warning: no current bounds for " + path);
				i.remove();
			} else {
				Rectangle end = getProjectedPathBounds(path);
				if (end == null) {
					// System.err.println("warning: no final bounds for " +
					// path);
					i.remove();
				} else if ((current.x != end.x) || (current.y != end.y)) {
					int xdelta = (end.x - current.x) / 2;
					int ydelta = (end.y - current.y) / 2;
					if (xdelta == 0)
						current.x = end.x;
					else
						current.x += xdelta;
					if (ydelta == 0)
						current.y = end.y;
					else
						current.y += ydelta;
					this.bounds.put(path, current);
					changed = true;
				}
			}
		}
		return changed;
	}

	protected TargetLocation getTargetLocation(Point where) {
		int x = where.x;
		int y = where.y;
		int size = this.tree.getRowCount();
		Rectangle appendBounds = this.tree.getRowBounds(size - 1);
		appendBounds.y += appendBounds.height;
		appendBounds.height = 0;
		int targetRow = this.tree.getClosestRowForLocation(x, y);
		int draggedRow = this.tree.getRowForPath(this.draggedPath);
		TreePath parentPath = null;
		int index = 0;
		if (targetRow == draggedRow) {
			parentPath = this.draggedPath.getParentPath();
			Object parent = parentPath.getLastPathComponent();
			Object node = this.draggedPath.getLastPathComponent();
			index = this.tree.getModel().getIndexOfChild(parent, node);
		} else if (targetRow == 0) {
			if (this.tree.isRootVisible()) {
				return null;
			}

			parentPath = new TreePath(this.tree.getModel().getRoot());
			index = 0;
		} else {
			TreePath pathUnderPoint = this.tree.getPathForRow(targetRow);
			if (pathUnderPoint == null)
				return null;
			for (Iterator<TreePath> i = this.springs.iterator(); i.hasNext();) {
				TreePath p = (TreePath) i.next();
				if (!p.isDescendant(pathUnderPoint)) {
					i.remove();
					this.tree.collapsePath(p);
				}
			}
			Object node = pathUnderPoint.getLastPathComponent();
			boolean underIsLeaf = this.tree.getModel().isLeaf(node);
			if (underIsLeaf) {
				parentPath = pathUnderPoint.getParentPath();
				Object parent = parentPath.getLastPathComponent();
				index = this.tree.getModel().getIndexOfChild(parent, node);
			} else if (draggedRow == targetRow - 1) {
				parentPath = pathUnderPoint;
				index = 0;
			} else {
				parentPath = pathUnderPoint.getParentPath();
				index = this.tree.getModel().getIndexOfChild(
						parentPath.getLastPathComponent(),
						pathUnderPoint.getLastPathComponent());
			}

		}

		return new TargetLocation(parentPath, index, targetRow);
	}

	public void setInsertionLocation(Point where) {
		if (!this.dragActive) {
			throw new IllegalStateException("Not dragging");
		}
		getPainter().requestFocus();
		this.tree.clearSelection();

		TargetLocation loc = getTargetLocation(where);
		TreePath parentPath = null;
		if ((loc != null) && (this.draggedPath != null)
				&& (canMove(this.draggedPath, loc.parentPath, loc.index))) {
			parentPath = loc.parentPath;
			setInsertionRow(loc.insertionRow);
		}

		this.dragImage.setLocation(where, parentPath);
	}

	protected int getInsertionRow() {
		return this.insertionRow;
	}

	private void setInsertionRow(int idx) {
		if (idx != this.insertionRow) {
			this.insertionRow = idx;
			repaint();
		}
	}

	private Rectangle getProjectedPathBounds(TreePath path) {
		Rectangle pathBounds = this.tree.getPathBounds(path);
		if (this.draggedPath != null) {
			int row = this.tree.getRowForPath(path);
			int removalRow = this.tree.getRowForPath(this.draggedPath);
			Rectangle draggedBounds = this.tree.getPathBounds(this.draggedPath);
			if (draggedBounds != null)
				if ((removalRow < row) && (row <= this.insertionRow))
					pathBounds.y -= draggedBounds.height;
				else if ((this.insertionRow <= row) && (row < removalRow)) {
					pathBounds.y += draggedBounds.height;
				}
		}
		return pathBounds;
	}

	private Rectangle getCurrentCellBounds(TreePath path) {
		Rectangle after = getProjectedPathBounds(path);
		Rectangle current = (Rectangle) this.bounds.get(path);
		if (current != null) {
			after.x = current.x;
			after.y = current.y;
		}
		return after;
	}

	public void paint(Graphics g) {
		boolean db = this.tree.isDoubleBuffered();
		this.tree.setDoubleBuffered(false);
		try {
			Rectangle b = getDecorationBounds();
			g.setColor(this.tree.getBackground());
			g.fillRect(b.x, b.y, b.width, b.height);
			int prevIndex = -1;
			Rectangle prevBounds = null;
			for (int i = 0; i < this.tree.getRowCount(); i++) {
				// TreePath path = this.tree.getPathForRow(i);
				// if (!path.equals(this.draggedPath)) {
				Rectangle r = getCurrentCellBounds(this.tree.getPathForRow(i));
				Rectangle r2 = this.tree.getRowBounds(i);
				if ((prevIndex != -1)
						&& (prevBounds.y + prevBounds.height < r.y)) {
					Rectangle space = new Rectangle(0, prevBounds.y
							+ prevBounds.height, r.x, r.y - prevBounds.y
							- prevBounds.height);
					for (int j = 0; j < space.height; j++) {
						Graphics g2 = g.create(space.x, space.y + j,
								space.width, 1);
						((Graphics2D) g2).translate(0, -r2.y - 1);
						this.tree.paint(g2);
					}
				}
				Graphics g2 = g.create(0, r.y, r.x + r.width, r.height);
				((Graphics2D) g2).translate(0, -r2.y);
				this.tree.paint(g2);
				prevIndex = i;
				prevBounds = r;
				// }
			}
			if (this.counter != null)
				this.counter.painted();
		} finally {
			this.tree.setDoubleBuffered(db);
		}
	}

	private final class Counter extends TimerTask {
		public boolean painted;

		private Counter() {
		}

		public synchronized void painted() {
			this.painted = true;
		}

		public void run() {
			if ((this.painted) && (AnimatorTree.this.reposition()))
				synchronized (this) {
					this.painted = false;
					AnimatorTree.this.repaint();
				}
		}
	}

	private final class GhostedDragImage extends AbstractComponentDecorator {
		private TreePath path;
		private Point location;
		private Point offset;

		public GhostedDragImage(TreePath path, Point origin) {
			super(tree);
			// super(JLayeredPane.DRAG_LAYER.intValue());
			// super(path);
			this.path = path;
			Rectangle b = AnimatorTree.this.tree.getPathBounds(path);
			this.location = origin;
			this.offset = new Point(origin.x - b.x, origin.y - b.y);
		}

		public void setLocation(Point where, TreePath parentPath) {
			this.location = new Point(where);
			Rectangle b = AnimatorTree.this.tree.getPathBounds(this.path);
			Rectangle lastRow = AnimatorTree.this.tree
					.getRowBounds(AnimatorTree.this.tree.getRowCount() - 1);
			int height = lastRow.y + lastRow.height;
			Point origin = new Point(b.x, this.location.y - this.offset.y);

			if (parentPath != null) {
				int count = this.path.getPathCount();
				if ((!AnimatorTree.this.tree.isRootVisible())
						|| (!AnimatorTree.this.tree.getShowsRootHandles()))
					count--;
				Insets insets = AnimatorTree.this.tree.getInsets();
				int delta = (origin.x - (insets != null ? insets.left : 0))
						/ count;
				b = AnimatorTree.this.tree.getPathBounds(parentPath);
				origin.x = (b.x + delta);
			}
			this.location.x = origin.x;
			this.location.y = Math.max(0, origin.y);
			this.location.y = Math.min(this.location.y, height - b.height);
			getPainter().repaint();
		}

		// public Point getLocation() {
		// return this.location;
		// }

		public void paint(Graphics g) {
			Rectangle b = AnimatorTree.this.tree.getPathBounds(this.path);
			g = g.create(this.location.x, this.location.y, b.width, b.height);
			((Graphics2D) g).translate(-b.x, -b.y);
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(3, 0.5F));
			AnimatorTree.this.tree.paint(g);
		}
	}

	protected class TargetLocation {
		public TreePath parentPath;
		public int index;
		public int insertionRow;

		public TargetLocation(TreePath path, int i, int row) {
			this.parentPath = path;
			this.index = i;
			this.insertionRow = row;
		}

		public String toString() {
			return this.parentPath.toString() + ":" + this.index + " ("
					+ this.insertionRow + ")";
		}
	}
}
