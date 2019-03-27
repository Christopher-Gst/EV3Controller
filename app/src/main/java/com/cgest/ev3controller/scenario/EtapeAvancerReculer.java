package com.cgest.ev3controller.scenario;

import com.cgest.ev3controller.Utile;
import com.cgest.ev3controller.capteur.Capteur;
import com.cgest.ev3controller.capteur.CapteurCouleur;
import com.cgest.ev3controller.capteur.CapteurProximite;

public abstract class EtapeAvancerReculer extends Etape implements EtapeParametrable, Cloneable {

    // Temps ou distance de parcours.
    private int valeur;
    // Unité de parcours : secondes ou cm.
    private int unite;
    public static int SECONDES = 0;
    public static int CM = 1;
    public static final int AVANCER = 0;
    public static final int RECULER = 1;

    // Contrainte de parcours : jusqu'à une certaine détection.
    private Capteur capteur;

    // Attributs par défaut en cas de constructeur vide.
    public final static int VALEUR_PAR_DEFAUT = 10;
    private final static int UNITE_PAR_DEFAUT = 0;
    private final static Capteur CAPTEUR_PAR_DEFAUT = null;

    // Soit le robot avance ou recule pendant une certaine durée ou sur une certaine distance.
    public EtapeAvancerReculer(int valeur, int unite) {
        this.valeur = valeur;
        this.unite = unite;
    }

    // Soit le robot avance ou recule jusqu'à une détection.
    public EtapeAvancerReculer(Capteur capteur) {
        this.capteur = capteur;
    }

    public EtapeAvancerReculer() {
        this.valeur = VALEUR_PAR_DEFAUT;
        this.unite = UNITE_PAR_DEFAUT;
        this.capteur = CAPTEUR_PAR_DEFAUT;
    }

    @Override
    public String getCode() {
        return (this instanceof EtapeAvancer ? "A" : "R") + "." + (capteur == null ? (valeur + "." + unite) : capteur.getCode());
    }

    public Capteur getCapteur() {
        return capteur;
    }

    public int getValeur() {
        return valeur;
    }

    public int getUnite() {
        return unite;
    }

    public void setCapteur(Capteur capteur) {
        this.capteur = capteur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public void setUnite(int unite) {
        this.unite = unite;
    }

    @Override
    public String toString() {
        return "EtapeAvancerReculer{ sens=" + (this instanceof EtapeAvancer ? "Avancer" : (this instanceof EtapeReculer ? "Reculer" : "?")) +
                ", valeur=" + valeur +
                ", unite=" + unite +
                ", capteur=" + capteur +
                '}';
    }

    @Override
    public Object getParamType() {
        if (capteur != null) return capteur.getParamType();
        return new Integer(0);
    }

    public String getNomImageDescription() {
        // Si on avance ou on recule pendant une durée et sur une distance...
        if (getCapteur() == null) {
            // Début du nom de l'image.
            String debutNom;
            if (getUnite() == SECONDES)
                debutNom = "icon_horloge";
            else
                debutNom = "icon_regle";
            return debutNom + super.getNomImageDescription();
        } else { // Sinon, si on avance / recule jusqu'à une détection d'un capteur...
            return getCapteur().getNomImageDescription();
        }
    }

    @Override
    public String getMessageEdition() {
        if (getCapteur() == null)
            // Si on avance pendant une durée...
            if (getUnite() == EtapeAvancerReculer.SECONDES)
                return "Saisissez la durée de déplacement :";
            else // Sinon, si on avance sur une distance...
                return "Saisissez la distance de déplacement :";
        else if (getCapteur() instanceof CapteurProximite)
            return "Saisissez la distance de détection :";
        else if (getCapteur() instanceof CapteurCouleur)
            return "Sélectionnez la couleur à détecter :";
        return "";
    }

    @Override
    public void setParametre(Object param) {
        // Si l'action n'est pas d'avancer/reculer jusqu'à détection de quelque chose...
        if (getCapteur() == null) {
            // Si le paramètre est un entier...
            if (param instanceof Integer)
                // Alors on modifie la valeur (distance ou durée).
                setValeur((Integer) param);
            // Sinon, si l'action est d'avancer/reculer jusqu'à détection de quelque chose...
        } else if (param instanceof Capteur)
            // On modifie le capteur avec celui passé en paramètre.
            setCapteur((Capteur) param);
    }

    @Override
    public Object getParametre() {
        // Si l'action n'est pas d'avancer/reculer jusqu'à détection de quelque chose...
        if (getCapteur() == null) {
            return valeur;
        } else {
            if (capteur instanceof CapteurCouleur)
                return ((CapteurCouleur) capteur).getCouleur();
            if (capteur instanceof CapteurProximite)
                return ((CapteurProximite) capteur).getDistanceDetection();
        }
        return null;
    }

    @Override
    public Object clone() {
        EtapeAvancerReculer clone = null;
        // On récupère l'instance à renvoyer par l'appel de la méthode super.clone()
        clone = (EtapeAvancerReculer) super.clone();
        clone.setValeur(valeur);
        clone.setUnite(unite);
        if (capteur != null)
            clone.setCapteur((Capteur) capteur.clone());
        // on renvoie le clone
        return clone;
    }

}