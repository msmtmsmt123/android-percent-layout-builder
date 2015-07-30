package com.xirtam.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TestSE extends JFrame implements MouseListener {

	private static final long serialVersionUID = 8344530105403542763L;
	private static TestSE root;
	private static JButton jButton;

	public TestSE() throws HeadlessException {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, 400, 400);
		this.addMouseListener(this);
	}

	public static void main(String[] args) {
		root = new TestSE();
		root.setVisible(true);
		root.getContentPane().setLayout(null);
		jButton = new JButton("asd") {
			private static final long serialVersionUID = 9025500602675270266L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(Color.black);
				g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 3, 3);
				paintChildren(g);
			}
		};
		jButton.setBounds(80, 80, 80, 80);
		root.getContentPane().add(jButton);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("click");
		jButton.setLocation(new Point(jButton.getLocation().x + 11, jButton
				.getLocation().y + 11));
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}
