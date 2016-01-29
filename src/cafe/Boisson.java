package cafe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import stock.StockIngredient;

public class Boisson {

    private int prix;
    private String nom;

    /**
     * Quantité de chaque ingredient
     */
    private HashMap<Ingredient, Integer> recette;

    public Boisson(String nom, int prix) {
        this.nom = nom;
        this.prix = prix;
        this.recette = new HashMap<>();

        for (Ingredient i : Ingredient.values()) {
            this.recette.put(i, 0);
        }
    }

    public String getNom() {
        return nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    /**
     * Modification d'un ingrédient de la boisson
     *
     * @param i Ingrédient
     * @param qte Quantité
     */
    public void setIngredient(Ingredient i, Integer qte) {
        this.recette.replace(i, qte);
    }

    /**
     * Tester la possiblité de la boisson en fonction du stock.
     * Pas de paramètre, car les stocks sont accessibles partout (Singleton)
     * 
     * @param stock Stock à utiliser
     * @return Vrai/Faux
     */
    public boolean estPossible(StockIngredient stock) {
        //return false;
        // TODO
        // Parcourir la liste des ingrédients
        
        boolean possible = true;
        
        for (Ingredient i : recette.keySet()) {
            int stockDispo = stock.getQuantite(i);
            int besoin = recette.get(i);
            if(stockDispo < besoin){
                possible = false;
                break;
            }
        }
        
        return possible;
    }

    /**
     * Méthode appelée lors de l'achat de la boisson Vérifie la possibilité, et
     * enlève les ingrédients du stock Calcul la monnaie à rendre et la rend.
     *
     * @param argentDonne Argent donné par l'acheteur
     * @param stock Stock à utiliser
     * @return L'argent à rendre
     * @throws cafe.MontantInsufisantException
     * @throws cafe.StockInsufisantException
     */
    public int acheter(int argentDonne, StockIngredient stock)
        throws MontantInsufisantException,
                StockInsufisantException
    {
        // TODO
        // Vérification de la possibilité
        
        int prix = getPrix();  // on ne sait jamais si un calcul
                               // de TVA traine dans un getter
        
        if(argentDonne < prix){
            throw new MontantInsufisantException(getPrix());
        }
        if(!estPossible(stock)){
            throw new StockInsufisantException(getIngredientManquant(stock));
        }
        // on est OK !
        
        
        // Enlèvement des ingrédients du stock.
        for(Ingredient i : recette.keySet()){
            stock.enleverQuantite(i, recette.get(i));
        }
        
        // Monnaie à rendre
        return argentDonne - prix;
    }

    @Override
    public String toString() {
        String res = this.nom + "(Prix:" + this.prix + ") Ingrédients{";
        Set set = recette.keySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Ingredient i = (Ingredient) it.next();
            res += i.toString() + ":" + recette.get(i) + "-";
        }
        res += "}";
        return res;
    }
    
    /**
     * Donne l'ingredient manquant en cas d'achat impossible
     * (pour cause de manque)
     * @param stock Stock de réference
     * @return l'ingrédient manquant
     */
    private Ingredient getIngredientManquant(StockIngredient stock){
        /* 
            Pas bien mais pas le choix en Java
            Rend la méthode plus robuste mais ne devrais 
            pas exister -> programmation défensive        
        */
        Ingredient manquant = null; 
                                     
        for(Ingredient i : recette.keySet()){
            if(stock.getQuantite(i) < recette.get(i)){
                manquant = i;
                break;
            }
        }
        return manquant;
    }

}
