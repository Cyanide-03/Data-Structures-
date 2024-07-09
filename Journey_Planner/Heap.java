import java.util.*;

public class Heap<T extends Comparable<T>> 
{
	ArrayList<T> data = new ArrayList<>();

	// This map stores the name of each node and the particular index of it in heap array
	HashMap<T, Integer> map = new HashMap<>();

	public void add(T item) 
	{
		data.add(item);   
		map.put(item, this.data.size() - 1);
		bubbleup(data.size() - 1);
	}

	public int getp(int index){
		return (index-1)/2;
	}

	public int getlc(int index){
		return 2*index+1;
	}

	public int getrc(int index){
		return 2*index+2;
	}

	// private void bubbleup(int ci) 
	// {
	// 	int pi = (ci - 1) / 2; // In 0 indexing
	// 	if (isLarger(data.get(ci), data.get(pi)) > 0) 
	// 	{
	// 		swap(pi, ci);
	// 		bubbleup(pi);
	// 	}
	// }

	private void bubbleup(int child){
		// int parent=(child-1)/2;
		// while(isLarger(data.get(child), data.get(getp(child))) > 0){
		while(data.get(child).compareTo(data.get(getp(child))) > 0){
			swap(getp(child), child);
			child=getp(child);
			// parent=(child-1)/2;
		}
	}

	private void swap(int i, int j) 
	{
		T ith = data.get(i);
		T jth = data.get(j);
		
		data.set(i, jth);
		data.set(j, ith);
		map.put(ith, j);
		map.put(jth, i);
	}

	public void display() 
	{
		System.out.println(data);
	}

	public int size() 
	{
		return this.data.size();
	}

	public boolean isEmpty() {
		return this.size() == 0;
	}

	// Removes the topmost element
	public T remove() 
	{
		swap(0, this.data.size() - 1);
		T rv = this.data.remove(this.data.size() - 1);
		bubbledown(0);

		map.remove(rv);
		return rv;
	}

	// private void bubbledown(int pi) 
	// {
	// 	int lci = 2 * pi + 1;
	// 	int rci = 2 * pi + 2;
	// 	int mini = pi;
	// 	if (lci < this.data.size() && isLarger(data.get(lci), data.get(mini)) > 0)
	// 	{
	// 		mini = lci;
	// 	}
	// 	if (rci < this.data.size() && isLarger(data.get(rci), data.get(mini)) > 0) 
	// 	{
	// 		mini = rci;
	// 	}	
	// 	if (mini != pi)
	// 	{
	// 		swap(mini, pi);
	// 		bubbledown(mini);
	// 	}
	// }

	private void bubbledown(int index){
		int lc=getlc(index);
		int j=lc;
		while(j<data.size()){
			if(j<data.size()-1 && isLarger(data.get(j+1), data.get(j))>0){
				j=j+1; // Here j will point to the minimum of the two child
			}
			if(isLarger(data.get(j), data.get(index))>0){
				swap(index, j);
				index=j;
				j=getlc(index);
			}
			else{
				break;
			}
		}
	}

	public T get() 
	{
		return this.data.get(0);
	}

	public int isLarger(T t, T o) 
	{
		return t.compareTo(o);
	}

	public void updatePriority(T pair) 
	{
		int index = map.get(pair);
		bubbleup(index);
	}
}