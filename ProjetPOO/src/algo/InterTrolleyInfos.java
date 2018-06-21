package algo;

import metier.Trolley;
/**
 * Classe permettant de stocker les informations concernant un mouvement d'une box
 * entre deux trolleys.
 */
public class InterTrolleyInfos {

    private Trolley oldTrolley;
    private Trolley newTrolley;
	private int oldPosition;
	private int newPosition;
	private double diffCout;

	public InterTrolleyInfos() {
		this.oldTrolley = new Trolley();
        this.newTrolley = new Trolley();
		this.oldPosition = -1;
		this.newPosition = -1;
		this.diffCout = Double.MAX_VALUE;
	}

	public InterTrolleyInfos(Trolley t1, Trolley t2, int oldPosition, int newPosition, double diffCout) {
		this.oldTrolley = t1;
        this.newTrolley = t2;
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
		this.diffCout = diffCout;
	}

	public InterTrolleyInfos(InterTrolleyInfos intraTourneeInfos) {
		this.oldTrolley = intraTourneeInfos.oldTrolley;
        this.newTrolley = intraTourneeInfos.newTrolley;
		this.oldPosition = intraTourneeInfos.oldPosition;
		this.newPosition = intraTourneeInfos.newPosition;
		this.diffCout = intraTourneeInfos.diffCout;
	}

	/**
     * Permet de réaliser un échange de boxes inter-trolleys.
     * @return boolean : indique si l'échange a été réalisé ou pas.
     */
    public boolean doEchangeInterTrolley() {
		if (this.diffCout == Double.MAX_VALUE) {
			return false;
		}
		if (this.newPosition == -1 || this.oldPosition == -1) {
			return false;
		}
		if (this.oldTrolley == null || this.newTrolley == null) {
			return false;
		} else {
			return this.oldTrolley.doEchangeInterTrolley(this);
		}
	}

    public Trolley getOldTrolley() {
        return oldTrolley;
    }

    public Trolley getNewTrolley() {
        return newTrolley;
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
        return "IntraTrolleyInfos{" + "trolley t1=" + oldTrolley.getIdTrolley() + ", trolley t2=" + newTrolley.getIdTrolley() + ", oldPosition=" + oldPosition + ", newPosition=" + newPosition + ", diffCout=" + diffCout + '}';
    }

}