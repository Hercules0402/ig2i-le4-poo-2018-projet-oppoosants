package algo;

import metier.Instance;

public class RechercheLocale {
    
    private Instance instance;

	public RechercheLocale(Instance instance) {
		this.instance = instance;
    }
    
    private boolean deplacementIntraBox() {
        return this.instance.deplacementIntraBox();
    }
    
    private boolean echangeIntraBox() {
		return this.instance.echangeIntraBox();
}
    
    public void rechercheLocale() {
        while(this.deplacementIntraBox()) {
            
        }
    }
}
