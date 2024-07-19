import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import Includes.*;
	
	public class Graph_M 
	{
		
		// src station-{nbd stations,edge weight}
		// kind of adjacency list
		static HashMap<String, Vertex> adj;

		public Graph_M(){
			adj = new HashMap<>();
		}

		public int numVetex(){
			return adj.size();
		}

		public boolean containsVertex(String vname){
			return adj.containsKey(vname);
		}

		public void addVertex(String vname){
			Vertex vtx = new Vertex();
			adj.put(vname, vtx);
		}

		public void removevertex(String vname){
			Vertex v=adj.get(vname);
			for(String i:v.nbrs.keySet()){
				Vertex nbrVtx=adj.get(i);
				nbrVtx.nbrs.remove(vname);
			}
			adj.remove(vname);
		}

		public int numEdges() 
		{
			ArrayList<String> keys = new ArrayList<>(adj.keySet());
			int count = 0;

			for (String key : keys) 
			{
				Vertex vtx = adj.get(key);
				count+=vtx.nbrs.size();
			}

			return count/2;
		}

		public boolean containsEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = adj.get(vname1);
			Vertex vtx2 = adj.get(vname2);
			
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return false;
			}

			return true;
		}

		public void addEdge(String vname1, String vname2, int value) 
		{
			if(!containsEdge(vname1, vname2)){ // This function run <=> there is NOT an edge between the two stations
				Vertex vtx1 = adj.get(vname1); 
				Vertex vtx2 = adj.get(vname2); 

				if (vtx1 == null || vtx2 == null) {
					return;
				}

				vtx1.nbrs.put(vname2, value);
				vtx2.nbrs.put(vname1, value);
			}
		}

		public void removeEdge(String vname1, String vname2) 
		{
			if(containsEdge(vname1, vname2)){ // This function rund iff there is an edge between the two stations
				Vertex vtx1 = adj.get(vname1);
				Vertex vtx2 = adj.get(vname2);
				
				//check if the vertices given or the edge between these vertices exist or not
				if (vtx1 == null || vtx2 == null) {
					return;
				}

				vtx1.nbrs.remove(vname2);
				vtx2.nbrs.remove(vname1);
			}
		}
			
		public void display_Map() 
		{
			System.out.println("\t Delhi Metro Map");
			System.out.println("\t------------------");
			System.out.println("-----------------------------------------\n");
			ArrayList<String> keys = new ArrayList<>(adj.keySet());

			for (String key : keys) 
			{
				String str = key + " =>\n";
				Vertex vtx = adj.get(key);
				ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());
				
				for (String nbr : vtxnbrs)
				{
					str = str + "\t" + nbr + "\t";
					if (nbr.length()<16)
						str = str + "\t";
					if (nbr.length()<8)
						str = str + "2\t";
					str = str + vtx.nbrs.get(nbr) + "\n";
				}
				System.out.println(str);
			}
			System.out.println("\t------------------");
			System.out.println("-----------------------------------------\n");
		}
		
		public void display_Stations() 
		{
			System.out.println("\n***********************************************************************\n");
			ArrayList<String> keys = new ArrayList<>(adj.keySet());
			int i=1;
			for(String key : keys) 
			{
				System.out.println(i + ". " + key);
				i++;
			}
			System.out.println("\n***********************************************************************\n");
		}

		public boolean hasPath(String vname1, String vname2){
			Queue<String> q=new LinkedList<>();
			q.add(vname1);

			while(!q.isEmpty()){
				String src=q.peek();
				q.poll();
				Vertex v1=adj.get(src);
				for(String nbr:v1.nbrs.keySet()){
					if(nbr.equals(vname2)){
						return true;
					}
					q.add(nbr);
				}
			}
			return false;
		}
		
		public int Dijkstra(String src,String dest,boolean nan){
			Heap<DijkstraPair> hp=new Heap<>();
			HashMap<String,DijkstraPair> hm=new HashMap<>();
			int val=0;

			for(String key:adj.keySet()){
				DijkstraPair dp=new DijkstraPair();
				dp.cost=Integer.MAX_VALUE;
				dp.vname=key;
				if(key.equals(src)){
					dp.cost=0;
					dp.psf=key;
					hp.add(dp);
				}
				hm.put(key,dp);
			}

			while(!hp.isEmpty()){
				DijkstraPair rem=hp.remove();
				String stat=rem.vname;
				int cost=rem.cost;
				
				if(stat.equals(dest)){
					val=cost;
					break;
				}

				hm.remove(stat);

				for(String nbr:adj.get(stat).nbrs.keySet()){
					if(hm.containsKey(nbr)){
						int edge=adj.get(stat).nbrs.get(nbr);

						int old_cost=hm.get(nbr).cost;
						int new_cost=0;
						if(nan){
							new_cost=cost+120+40*edge;
						}
						else{
							new_cost=edge+cost;
						}
						if(old_cost>new_cost){
							DijkstraPair np=new DijkstraPair();
							np.vname=nbr;
							np.cost=new_cost;
							np.psf=rem.psf+nbr;
							hm.put(nbr,np);
							hp.add(np);
						}
					}
				}
			}
			return val;
		}

		public String get_Minimum_Distance_path(String src,String dst){
			HashMap<String,String> par=new HashMap<>(); // {child-parent}
			HashMap<String,DijkstraPair> hm=new HashMap<>();
			Heap<DijkstraPair> hp=new Heap<>();


			for(String key:adj.keySet()){
				DijkstraPair dp=new DijkstraPair();
				dp.vname=key;
				dp.cost=Integer.MAX_VALUE;
				par.put(key, "null");
				if(key.equals(src)){
					dp.cost=0;
					dp.psf=key;
					hp.add(dp);
					par.replace(src,"origin");
				}
				hm.put(key, dp);
			}

			int val=0;
			while(!hp.isEmpty()){
				DijkstraPair rem=hp.remove();
				String stat=rem.vname;
				int cost=rem.cost;

				if(stat.equals(dst)){
					val=cost;
					break;
				}

				hm.remove(stat);

				Vertex v=adj.get(stat);
				for(String nbr:v.nbrs.keySet()){
					if(hm.containsKey(nbr)){
						int edge=v.nbrs.get(nbr);

						int old_cost=hm.get(nbr).cost;
						int new_cost=edge+cost;

						if(old_cost>new_cost){
							DijkstraPair np=new DijkstraPair();
							np.vname=nbr;
							np.cost=new_cost;
							np.psf=rem.psf+nbr;
							par.remove(nbr);
							par.put(nbr,stat);
							hm.put(nbr,np);
							hp.add(np);
						}
					}	
				}
			}
			String key=dst;
			String ans="";
			while(key!="origin"){
				ans=key+"  "+ans;
				key=par.get(key);
			}
			ans+=Integer.toString(val);
			return ans;
		}

		public String get_Minimum_time_path(String src,String dst){
			HashMap<String,String> par=new HashMap<>(); // {child-parent}
			HashMap<String,DijkstraPair> hm=new HashMap<>();
			Heap<DijkstraPair> hp=new Heap<>();


			for(String key:adj.keySet()){
				DijkstraPair dp=new DijkstraPair();
				dp.vname=key;
				dp.cost=Integer.MAX_VALUE;
				par.put(key, "null");
				if(key.equals(src)){
					dp.cost=0;
					dp.psf=key;
					hp.add(dp);
					par.replace(src,"origin");
				}
				hm.put(key, dp);
			}

			int val=0;
			while(!hp.isEmpty()){
				DijkstraPair rem=hp.remove();
				String stat=rem.vname;
				int cost=rem.cost;

				if(stat.equals(dst)){
					val=cost;
					break;
				}

				hm.remove(stat);

				Vertex v=adj.get(stat);
				for(String nbr:v.nbrs.keySet()){
					if(hm.containsKey(nbr)){
						int edge=v.nbrs.get(nbr);

						int old_cost=hm.get(nbr).cost;
						int new_cost=cost+120+40*edge;

						if(old_cost>new_cost){
							DijkstraPair np=new DijkstraPair();
							np.vname=nbr;
							np.cost=new_cost;
							np.psf=rem.psf+nbr;
							par.remove(nbr);
							par.put(nbr,stat);
							hm.put(nbr,np);
							hp.add(np);
						}
					}	
				}
			}
			String key=dst;
			String ans="";
			while(key!="origin"){
				ans=key+"  "+ans;
				key=par.get(key);
			}
			// Double minutes = Math.ceil((double)val / 60);
			ans+=Integer.toString(val/60);
			return ans;
		}

		public ArrayList<String> Get_Interchanges(String s){
			// s-{source,..........,Destination,min_distance}
			String str[]=s.split("  ");
			ArrayList<String> ans=new ArrayList<>();
			ans.add(str[0]);
			int count=0;

			for(int i=1;i<str.length-2;i++){
				String curr=str[i].substring(str[i].indexOf('~')+1);

				if(curr.length()==2){
					String prev=str[i-1].substring(str[i-1].indexOf('~')+1);
					String next=str[i+1].substring(str[i+1].indexOf('~')+1);

					if(prev.equals(next)){
						ans.add(str[i]);
					}
					else{
						ans.add(str[i]+"=>"+str[i+1]);
						i++;
						count++;
					}

				}
				else{
					ans.add(str[i]);
				}
			}
			ans.add(Integer.toString(count));
			ans.add(str[str.length-1]);
			return ans;
		}
		
		public static void Create_Metro_Map(Graph_M g)
		{
			g.addVertex("Noida Sector 62~B");
			g.addVertex("Botanical Garden~B");
			g.addVertex("Yamuna Bank~B");
			g.addVertex("Rajiv Chowk~BY");
			g.addVertex("Vaishali~B");
			g.addVertex("Moti Nagar~B");
			g.addVertex("Janak Puri West~BO");
			g.addVertex("Dwarka Sector 21~B");
			g.addVertex("Huda City Center~Y");
			g.addVertex("Saket~Y");
			g.addVertex("Vishwavidyalaya~Y");
			g.addVertex("Chandni Chowk~Y");
			g.addVertex("New Delhi~YO");
			g.addVertex("AIIMS~Y");
			g.addVertex("Shivaji Stadium~O");
			g.addVertex("DDS Campus~O");
			g.addVertex("IGI Airport~O");
			g.addVertex("Rajouri Garden~BP");
			g.addVertex("Netaji Subhash Place~PR");
			g.addVertex("Punjabi Bagh West~P");
			
			g.addEdge("Noida Sector 62~B", "Botanical Garden~B", 8);
			g.addEdge("Botanical Garden~B", "Yamuna Bank~B", 10);
			g.addEdge("Yamuna Bank~B", "Vaishali~B", 8);
			g.addEdge("Yamuna Bank~B", "Rajiv Chowk~BY", 6);
			g.addEdge("Rajiv Chowk~BY", "Moti Nagar~B", 9);
			g.addEdge("Moti Nagar~B", "Janak Puri West~BO", 7);
			g.addEdge("Janak Puri West~BO", "Dwarka Sector 21~B", 6);
			g.addEdge("Huda City Center~Y", "Saket~Y", 15);
			g.addEdge("Saket~Y", "AIIMS~Y", 6);
			g.addEdge("AIIMS~Y", "Rajiv Chowk~BY", 7);
			g.addEdge("Rajiv Chowk~BY", "New Delhi~YO", 1);
			g.addEdge("New Delhi~YO", "Chandni Chowk~Y", 2);
			g.addEdge("Chandni Chowk~Y", "Vishwavidyalaya~Y", 5);
			g.addEdge("New Delhi~YO", "Shivaji Stadium~O", 2);
			g.addEdge("Shivaji Stadium~O", "DDS Campus~O", 7);
			g.addEdge("DDS Campus~O", "IGI Airport~O", 8);
			g.addEdge("Moti Nagar~B", "Rajouri Garden~BP", 2);
			g.addEdge("Punjabi Bagh West~P", "Rajouri Garden~BP", 2);
			g.addEdge("Punjabi Bagh West~P", "Netaji Subhash Place~PR", 3);
		}
		
		public static void main(String[] args) throws IOException, InterruptedException
		{
			Graph_M g = new Graph_M();
			Create_Metro_Map(g);
			
			System.out.println("\n\t\t\t****WELCOME TO THE METRO APP*****");
			TimeUnit.SECONDS.sleep(1);

			BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

			//STARTING SWITCH CASE
			while(true)
			{
				System.out.println("\t\t\t\t~~LIST OF ACTIONS~~\n\n");
				System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
				System.out.println("2. SHOW THE METRO MAP");
				System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("4. GET SHORTEST TIME TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("7. EXIT THE MENU");
				TimeUnit.SECONDS.sleep(1);
				System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 7) : ");
				int choice = -1;
				try {
					choice = Integer.parseInt(inp.readLine());
				} catch(Exception e) {
					// default will handle
				}
				System.out.print("\n***********************************************************\n");
				if(choice == 7)
				{
					System.exit(0);
				}
				switch(choice)
				{
				case 1:
					g.display_Stations();
					break;
			
				case 2:
					g.display_Map();
					break;
				
				case 3:
					System.out.print("ENTER THE SOURCE STATION: ");
					String st1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String st2 = inp.readLine();
				
					if(!g.containsVertex(st1) || !g.containsVertex(st2) || !g.hasPath(st1, st2))
						System.out.println("THE INPUTS ARE INVALID");
					else
					System.out.println("SHORTEST DISTANCE FROM "+st1+" TO "+st2+" IS "+g.Dijkstra(st1, st2, false)+"Km\n");
					break;
				
				case 4:
					System.out.print("ENTER THE SOURCE STATION: ");
					String sat1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String sat2 = inp.readLine();
				
					System.out.println("SHORTEST TIME FROM "+sat1+" TO "+sat2+" IS "+g.Dijkstra(sat1, sat2, true)/60+" MINUTES\n\n");
					break;
				
				case 5:
					System.out.print("ENTER THE SOURCE STATION: ");
					String s1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String s2 = inp.readLine();
				
					if(!g.containsVertex(s1) || !g.containsVertex(s2) || !g.hasPath(s1, s2))
						System.out.println("THE INPUTS ARE INVALID");
					else 
					{
						// System.out.println(g.get_Minimum_Distance_path(s1, s2));
						ArrayList<String> str = g.Get_Interchanges(g.get_Minimum_Distance_path(s1, s2));
						// String res[] = g.Get_Minimum_Distance_path(s1, s2).split("  ");
						// for(String i:str){
						// 	System.out.println(i);
						// }
						int len = str.size();
						System.out.println("SOURCE STATION : " + s1);
						System.out.println("DESTINATION STATION : " + s2);
						System.out.println("DISTANCE : " + str.get(len-1));
						System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
						System.out.println("~~~~~~~~~~~~~");
						System.out.println("START  ==>  " + str.get(0));
						for(int i=1; i<len-3; i++)
						{
							System.out.println(str.get(i));
						}
						System.out.print(str.get(len-3) + "   ==>    END");
						System.out.println("\n~~~~~~~~~~~~~");
					}
					break;
				
				case 6:
					System.out.print("ENTER THE SOURCE STATION: ");
					String ss1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String ss2 = inp.readLine();
				
					if(!g.containsVertex(ss1) || !g.containsVertex(ss2) || !g.hasPath(ss1, ss2))
						System.out.println("THE INPUTS ARE INVALID");
					else
					{
						ArrayList<String> ans = g.Get_Interchanges(g.get_Minimum_time_path(ss1, ss2));
						int length = ans.size();
						System.out.println("SOURCE STATION : " + ss1);
						System.out.println("DESTINATION STATION : " + ss2);
						System.out.println("TIME : " + ans.get(length-1)+" MINUTES");
						System.out.println("NUMBER OF INTERCHANGES : " + ans.get(length-2));
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						System.out.print("START  ==>  " + ans.get(0) + " ==>  ");
						for(int i=1; i<length-3; i++)
						{
							System.out.println(ans.get(i));
						}
						System.out.print(ans.get(length-3) + "   ==>    END");
						System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					}
					break;	
               	         default:
                    	        System.out.println("Please enter a valid option! ");
                        	    System.out.println("The options you can choose are from 1 to 6. ");
				}
			}
		}	
	}