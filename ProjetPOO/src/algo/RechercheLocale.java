package algo;

import metier.Instance;

public class RechercheLocale {

    private Instance instance;
    public static double diffCout = -Double.MAX_VALUE;

	public RechercheLocale(Instance instance) {
		this.instance = instance;
    }

    /**
     * Permet d'échanger des boxes dans différents trolleys (inter).
     * @return boolean : indique si l'échange a été réalisé ou pas
     */
    public boolean echangeInterTrolley() {
		return this.instance.echangeInterTrolley();
    }
   
    /**
     * Permet de récupérer l'instance.
     * @return Instance
     */
    public Instance getNewInstance() {
        return this.instance;
    }
}
