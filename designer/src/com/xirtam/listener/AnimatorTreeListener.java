package com.xirtam.listener;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.xirtam.data.AddIntoTree;
import com.xirtam.data.AnimatorTree;
import com.xirtam.utils.UIUtlis;

public class AnimatorTreeListener extends MouseAdapter {
	private AnimatorTree smoother;
	private boolean dragActive;
	private Point origin;
	private AddIntoTree intoTree;

	public AnimatorTreeListener(AnimatorTree smoother, AddIntoTree intoTree) {
		this.smoother = smoother;
		this.intoTree = intoTree;
	}

	private boolean sufficientMove(Point where) {
		int dx = Math.abs(this.origin.x - where.x);
		int dy = Math.abs(this.origin.y - where.y);
		return Math.sqrt(dx * dx + dy * dy) > 5.0D;
	}

	public void mousePressed(MouseEvent e) {
		this.origin = e.getPoint();
		if (UIUtlis.isShiftPressing) {
			intoTree.startDrag(e.getPoint());
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (UIUtlis.isShiftPressing) {
			intoTree.endDrag(e.getPoint());
		} else {
			if (this.dragActive) {
				this.smoother.endDrag(e.getPoint());
				this.dragActive = false;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (UIUtlis.isShiftPressing) {

		} else {
			if (this.dragActive) {
				this.smoother.setInsertionLocation(e.getPoint());
			} else if (sufficientMove(e.getPoint()))
				this.dragActive = this.smoother.startDrag(this.origin);
		}
	}

	public void mouseExited(MouseEvent e) {
		if (UIUtlis.isShiftPressing) {

		} else {
			if (this.dragActive)
				this.smoother.setInsertionLocation(e.getPoint());
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (UIUtlis.isShiftPressing) {

		} else {
			if (this.dragActive)
				this.smoother.setInsertionLocation(e.getPoint());
		}
	}

	public void mouseMoved(MouseEvent e) {
	}
}
