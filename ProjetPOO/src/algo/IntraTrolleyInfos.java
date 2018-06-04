package algo;

import metier.Trolley;

public class IntraTrolleyInfos {
    
    private Trolley trolley;
	private int oldPosition;
	private int newPosition;
	private double diffCout;

	public IntraTrolleyInfos() {
		this.trolley = null;
		this.oldPosition = -1;
		this.newPosition = -1;
		this.diffCout = Double.MAX_VALUE;
	}

	public IntraTrolleyInfos(Trolley trolley, int oldPosition, int newPosition, double diffCout) {
		this.trolley = trolley;
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
		this.diffCout = diffCout;
	}

	public IntraTrolleyInfos(IntraTrolleyInfos intraTourneeInfos) {
		this.trolley = intraTourneeInfos.trolley;
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

		if (this.trolley == null) {
			return false;
		} else {
			return this.trolley.doDeplacementIntraTrolley(this);
		}
	}

	public boolean doEchangeIntraTrolley() {
		if (this.diffCout == Double.MAX_VALUE) {
			return false;
		}
		if (this.newPosition == -1 || this.oldPosition == -1) {
			return false;
		}

		if (this.trolley == null) {
			return false;
		} else {
			return this.trolley.doEchangeIntraTrolley(this);
		}
	}

	public Trolley getTrolley() {
		return trolley;
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

    @Override
    public String toString() {
        return "IntraTrolleyInfos{" + "trolley=" + trolley.getIdTrolley() + ", oldPosition=" + oldPosition + ", newPosition=" + newPosition + ", diffCout=" + diffCout + '}';
    }
    
    
}