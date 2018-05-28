package algo;

import metier.Box;

public class IntraTrolleyInfos {
    
    private Box box;
	private int oldPosition;
	private int newPosition;
	private double diffCout;

	public IntraTrolleyInfos() {
		this.box = null;
		this.oldPosition = -1;
		this.newPosition = -1;
		this.diffCout = Double.MAX_VALUE;
	}

	public IntraTrolleyInfos(Box box, int oldPosition, int newPosition, double diffCout) {
		this.box = box;
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
		this.diffCout = diffCout;
	}

	public IntraTrolleyInfos(IntraTrolleyInfos intraTourneeInfos) {
		this.box = intraTourneeInfos.box;
		this.oldPosition = intraTourneeInfos.oldPosition;
		this.newPosition = intraTourneeInfos.newPosition;
		this.diffCout = intraTourneeInfos.diffCout;
	}

	public boolean doDeplacementIntraTrolley() {
		if (this.diffCout == Double.MAX_VALUE) {
			return false;
		}
		if (this.newPosition == -1 || this.oldPosition == -1) {
			return false;
		}

		if (this.box == null) {
			return false;
		} else {
			return this.box.doDeplacementIntraTrolley(this);
		}
	}

	public boolean doEchangeIntraTrolley() {
		if (this.diffCout == Double.MAX_VALUE) {
			return false;
		}
		if (this.newPosition == -1 || this.oldPosition == -1) {
			return false;
		}

		if (this.box == null) {
			return false;
		} else {
			return this.box.doEchangeIntraTrolley(this);
		}
	}

	public Box getBox() {
		return box;
	}

	public int getOldPosition() {
		return oldPosition;
	}

	public int getNewPosition() {
		return newPosition;
	}

	public double getDiffCout() {
		return diffCout;
    }
}