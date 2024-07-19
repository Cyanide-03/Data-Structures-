import java.util.*;

public class Heap<T extends Comparable<T>> 
{
	ArrayList<T> data = new ArrayList<>();

	// This map stores the name of each node and the particular index of it in heap array
	HashMap<T, Integer> map = new HashMap<>();

	public void add(T item){
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

	private void bubbleup(int child){
		while(data.get(child).compareTo(data.get(getp(child))) > 0){
			swap(getp(child), child);
			child=getp(child);
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
}