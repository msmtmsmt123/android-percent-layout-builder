package com.xirtam.data;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public abstract class AbstractComponentDecorator {
	public static final int TOP = 0;
	// private static final int BOTTOM = -1;
	private Point originOffset = new Point(0, 0);
	private Painter painter;
	private JComponent component;
	private Container parent;
	private Listener listener;
	private int layerOffset;
	private int position;
	private Rectangle bounds;
	private static Field nComponents;

	static {
		try {
			nComponents = Container.class.getDeclaredField("ncomponents");
			nComponents.setAccessible(true);
		} catch (Exception e) {
			nComponents = null;
		}
	}

	public AbstractComponentDecorator(JComponent c) {
		this(c, 1);
	}

	public AbstractComponentDecorator(JComponent c, int layerOffset) {
		this(c, layerOffset, 0);
	}

	public AbstractComponentDecorator(JComponent c, int layerOffset,
			int position) {
		this.component = c;
		this.layerOffset = layerOffset;
		this.position = position;
		this.bounds = null;
		this.parent = c.getParent();
		this.painter = new Painter();
		this.listener = new Listener();
		this.component.addHierarchyListener(this.listener);
		this.component.addComponentListener(this.listener);
		this.component.addPropertyChangeListener(this.listener);
		attach();
	}

	public void setToolTipText(String text) {
		this.painter.setToolTipText(text);
	}

	public String getToolTipText() {
		return this.painter.getToolTipText();
	}

	public String getToolTipText(MouseEvent e) {
		return getToolTipText();
	}

	public void setVisible(boolean visible) {
		this.painter.setVisible(visible);
	}

	protected void attach() {
		Window w = SwingUtilities.getWindowAncestor(this.component);
		if ((w instanceof RootPaneContainer)) {
			JLayeredPane lp = ((RootPaneContainer) w).getLayeredPane();
			Component layeredChild = this.component;
			int layer = JLayeredPane.DRAG_LAYER.intValue();
			if ((this instanceof BackgroundPainter)) {
				layer = ((BackgroundPainter) this).layer;
				this.painter.setDecoratedLayer(layer);
			} else if (layeredChild == lp) {
				this.painter.setDecoratedLayer(layer);
			} else {
				while (layeredChild.getParent() != lp) {
					layeredChild = layeredChild.getParent();
				}
				int base = lp.getLayer(layeredChild);

				layer = base + this.layerOffset;
				if (this.layerOffset < 0) {
					BackgroundPainter bp = (BackgroundPainter) lp
							.getClientProperty(BackgroundPainter.key(base));
					if (bp == null) {
						bp = new BackgroundPainter(lp, base);
					}
				}
				this.painter.setDecoratedLayer(base);
			}
			lp.add(this.painter, new Integer(layer), this.position);
		} else {
			Container parent = this.painter.getParent();
			if (parent != null) {
				parent.remove(this.painter);
			}
		}

		if (this.parent != null) {
			this.parent.removeComponentListener(this.listener);
		}
		this.parent = this.component.getParent();
		if (this.parent != null) {
			this.parent.addComponentListener(this.listener);
		}
		synch();
	}

	protected void synch() {
		Container painterParent = this.painter.getParent();
		if (painterParent != null) {
			Rectangle visible = getVisibleRect(getComponent());
			Rectangle decorated = getDecorationBounds();

			this.originOffset.x = decorated.x;
			this.originOffset.y = decorated.y;

			Rectangle clipped = decorated.intersection(visible);
			if (decorated.x < visible.x)
				this.originOffset.x += visible.x - decorated.x;
			if (decorated.y < visible.y)
				this.originOffset.y += visible.y - decorated.y;
			Point pt = SwingUtilities.convertPoint(this.component, clipped.x,
					clipped.y, painterParent);
			if ((clipped.width <= 0) || (clipped.height <= 0)) {
				setVisible(false);
			} else {
				setPainterBounds(pt.x, pt.y, clipped.width, clipped.height);
				setVisible(true);
			}
		}
		this.painter.revalidate();
		this.painter.repaint();
	}

	private Rectangle getVisibleRect(JComponent component) {
		Rectangle visible = component.getVisibleRect();
		if ((visible.x != 0) || (visible.y != 0)
				|| (visible.width != component.getWidth())
				|| (visible.height != component.getHeight())) {
			return visible;
		}
		Container parent = component.getParent();
		if ((parent instanceof JComponent)) {
			visible = ((JComponent) parent).getVisibleRect();
			visible.x -= component.getX();
			visible.y -= component.getY();
		}
		return visible;
	}

	public void setDecorationBounds(int x, int y, int w, int h) {
		if (this.bounds == null) {
			this.bounds = new Rectangle(x, y, w, h);
		} else {
			this.bounds.setBounds(x, y, w, h);
		}
		synch();
	}

	public void setDecorationBounds(Rectangle bounds) {
		if (this.bounds == null) {
			this.bounds = bounds;
		} else {
			this.bounds.setBounds(bounds);
		}
		synch();
	}

	protected Rectangle getDecorationBounds() {
		if (this.bounds == null) {
			return new Rectangle(0, 0, getComponent().getWidth(),
					getComponent().getHeight());
		}
		return this.bounds;
	}

	private void setPainterBounds(int x, int y, int w, int h) {
		this.painter.setLocation(x, y);
		this.painter.setSize(w, h);
		repaint();
	}

	protected JComponent getComponent() {
		return this.component;
	}

	protected JComponent getPainter() {
		return this.painter;
	}

	public void repaint() {
		this.component.repaint();
		this.painter.repaint();
	}

	public void dispose() {
		this.component.removeHierarchyListener(this.listener);
		this.component.removeComponentListener(this.listener);
		this.component.removePropertyChangeListener(this.listener);
		if (this.parent != null) {
			this.parent.removeComponentListener(this.listener);
		}
		Container painterParent = this.painter.getParent();
		if (painterParent != null) {
			Rectangle bounds = this.painter.getBounds();
			painterParent.remove(this.painter);
			painterParent.repaint(bounds.x, bounds.y, bounds.width,
					bounds.height);
		}
		this.component.repaint();
		this.component = null;
	}

	public abstract void paint(Graphics paramGraphics);

	public String toString() {
		return super.toString() + " on " + getComponent();
	}

	private static boolean useSimpleBackground() {
		return nComponents == null;
	}

	private static class BackgroundPainter extends AbstractComponentDecorator {
		private String key;
		private int layer;

		private static String key(int layer) {
			return "backgroundPainter" + layer;
		}

		public BackgroundPainter(JLayeredPane p, int layer) {
			super(p, layer);
			// super
			this.layer = layer;
			this.key = key(layer);
			p.putClientProperty(this.key, this);
		}

		private int hideChildren(Container c) {
			if (c == null)
				return 0;
			int value = c.getComponentCount();
			try {
				AbstractComponentDecorator.nComponents.set(c, new Integer(0));
			} catch (Exception e) {
				return c.getComponentCount();
			}
			return value;
		}

		private void restoreChildren(Container c, int count) {
			if (c != null)
				try {
					AbstractComponentDecorator.nComponents.set(c, new Integer(
							count));
				} catch (Exception localException) {
				}
		}

		private void paintBackground(Graphics g, Component parent, JComponent jc) {
			int x = jc.getX();
			int y = jc.getY();
			int w = jc.getWidth();
			int h = jc.getHeight();
			paintBackground(g.create(x, y, w, h), jc);
		}

		private void paintBackground(Graphics g, JComponent jc) {
			if (jc.isOpaque()) {
				if (AbstractComponentDecorator.useSimpleBackground()) {
					g.setColor(jc.getBackground());
					g.fillRect(0, 0, jc.getWidth(), jc.getHeight());
				} else {
					int count = hideChildren(jc);
					boolean db = jc.isDoubleBuffered();
					if (db)
						jc.setDoubleBuffered(false);
					jc.paint(g);
					if (db)
						jc.setDoubleBuffered(true);
					restoreChildren(jc, count);
				}
			}
			Component[] kids = jc.getComponents();
			for (int i = 0; i < kids.length; i++)
				if ((kids[i] instanceof JComponent))
					paintBackground(g, jc, (JComponent) kids[i]);
		}

		private List<Component> findOpaque(Component root) {
			List<Component> list = new ArrayList<Component>();
			if ((root.isOpaque()) && ((root instanceof JComponent))) {
				list.add(root);
				((JComponent) root).setOpaque(false);
			}
			if ((root instanceof Container)) {
				Component[] kids = ((Container) root).getComponents();
				for (int i = 0; i < kids.length; i++) {
					list.addAll(findOpaque(kids[i]));
				}
			}
			return list;
		}

		private List<Component> findDoubleBuffered(Component root) {
			List<Component> list = new ArrayList<Component>();
			if ((root.isDoubleBuffered()) && ((root instanceof JComponent))) {
				list.add(root);
				((JComponent) root).setDoubleBuffered(false);
			}
			if ((root instanceof Container)) {
				Component[] kids = ((Container) root).getComponents();
				for (int i = 0; i < kids.length; i++) {
					list.addAll(findDoubleBuffered(kids[i]));
				}
			}
			return list;
		}

		private void paintForeground(Graphics g, JComponent jc) {
			List<Component> opaque = findOpaque(jc);
			List<Component> db = findDoubleBuffered(jc);
			jc.paint(g);
			for (Iterator<Component> i = opaque.iterator(); i.hasNext();) {
				((JComponent) i.next()).setOpaque(true);
			}
			for (Iterator<Component> i = db.iterator(); i.hasNext();)
				((JComponent) i.next()).setDoubleBuffered(true);
		}

		public void paint(Graphics g) {
			JLayeredPane lp = (JLayeredPane) getComponent();
			Component[] kids = lp.getComponents();

			Area area = new Area();
			List<Component> painters = new ArrayList<Component>();
			List<Component> components = new ArrayList<Component>();
			for (int i = kids.length - 1; i >= 0; i--) {
				if ((kids[i] instanceof Painter)) {
					Painter p = (Painter) kids[i];
					if ((p.isBackgroundDecoration())
							&& (p.getDecoratedLayer() == this.layer)
							&& (p.isShowing())) {
						painters.add(p);
						area.add(new Area(p.getBounds()));
					}
				} else if ((lp.getLayer(kids[i]) == this.layer)
						&& ((kids[i] instanceof JComponent))) {
					components.add(kids[i]);
				}
			}
			if (painters.size() == 0) {
				dispose();
				return;
			}
			g.setClip(area);

			for (Iterator<Component> i = components.iterator(); i.hasNext();) {
				JComponent c = (JComponent) i.next();
				paintBackground(g, lp, c);
			}

			// for (Iterator<Component> i = painters.iterator(); i.hasNext();) {
			// AbstractComponentDecorator.Painter p =
			// (AbstractComponentDecorator.Painter) i
			// .next();
			// p.paint(g.create(p.getX(), p.getY(), p.getWidth(),
			// p.getHeight()));
			// }

			for (Iterator<Component> i = components.iterator(); i.hasNext();) {
				JComponent c = (JComponent) i.next();
				paintForeground(
						g.create(c.getX(), c.getY(), c.getWidth(),
								c.getHeight()), c);
			}
		}

		public void dispose() {
			getComponent().putClientProperty(this.key, null);
			super.dispose();
		}

		public String toString() {
			return this.key + " on " + getComponent();
		}
	}

	private final class Listener extends ComponentAdapter implements
			HierarchyListener, PropertyChangeListener {
		private Listener() {
		}

		public void hierarchyChanged(HierarchyEvent e) {
			if ((e.getChangeFlags() & 1L) != 0L)
				AbstractComponentDecorator.this.attach();
		}

		public void propertyChange(PropertyChangeEvent e) {
			if ("layeredContainerLayer".equals(e.getPropertyName()))
				AbstractComponentDecorator.this.attach();
		}

		public void componentMoved(ComponentEvent e) {
			AbstractComponentDecorator.this.attach();
		}

		public void componentResized(ComponentEvent e) {
			AbstractComponentDecorator.this.attach();
		}

		public void componentHidden(ComponentEvent e) {
			AbstractComponentDecorator.this.setVisible(false);
		}

		public void componentShown(ComponentEvent e) {
			AbstractComponentDecorator.this.setVisible(true);
		}
	}

	private class Painter extends JComponent {
		private static final long serialVersionUID = 7565537986672340251L;
		private int base;

		private Painter() {
		}

		public void setDecoratedLayer(int base) {
			this.base = base;
		}

		public int getDecoratedLayer() {
			return this.base;
		}

		public boolean isBackgroundDecoration() {
			return AbstractComponentDecorator.this.layerOffset < 0;
		}

		public void paint(Graphics g) {
			if (!AbstractComponentDecorator.this.component.isShowing())
				return;
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform xform = g2d.getTransform();
			try {
				g2d.translate(-AbstractComponentDecorator.this.originOffset.x,
						-AbstractComponentDecorator.this.originOffset.y);
				AbstractComponentDecorator.this.paint(g);
			} finally {
				g2d.setTransform(xform);
			}
		}

		public String getToolTipText(MouseEvent e) {
			return AbstractComponentDecorator.this.getToolTipText(e);
		}

		public String toString() {
			return "Painter for " + AbstractComponentDecorator.this;
		}
	}
}