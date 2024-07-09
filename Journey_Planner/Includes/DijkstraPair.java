package Includes;

public class DijkstraPair implements Comparable<DijkstraPair> {
    public String vname;
    public String psf;
    public int cost;

    /*
    The compareTo method is defined in Java.lang.Comparable.
    Here, we override the method because the conventional compareTo method
    is used to compare strings,integers and other primitive data types. But
    here in this case, we intend to compare two objects of DijkstraPair class.
    */ 

    /*
    Removing the overriden method gives us this errror:
    The type Graph_M.DijkstraPair must implement the inherited abstract method 
    Comparable<Graph_M.DijkstraPair>.compareTo(Graph_M.DijkstraPair)

    This is because DijkstraPair is not an abstract class and implements Comparable interface which has an abstract 
    method compareTo. In order to make our class concrete(a class which provides implementation for all its methods)
    we have to override the method compareTo
        */
    @Override
    public int compareTo(DijkstraPair o) 
    {
        return o.cost - this.cost;
    }
}
