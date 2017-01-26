
class RBFTest {
	public static void main(String args[]){
		final double trainingInput[][]={{1.0},{2.0},{3.0}};  //1 neurone d'entrée
		final double trainingOutput[][]={{1.1},{1.8},{3.1}}; //1 neurone de sortie

		RBF[] funcs={  //3 neurones dans la couche cachée a*1+b*x+c*x*x
			new RBF(){
				public double eval(Matrix x){//1
					return 1.0;
				}
			},
			new RBF(){
				public double eval(Matrix x){//x
					return x.getValue(0,0);
				}
			},
			new RBF(){
				public double eval(Matrix x){//x*x
					return x.getValue(0,0)*x.getValue(0,0);
				}
			}
		};

		RBFNetwork net=new RBFNetwork(trainingInput,trainingOutput,funcs);

		for(double x=-4.0;x<=4.0;x+=0.1){
			double [][]temp={{x}};
			Matrix res=net.eval(new Matrix(temp));
			String mes=""+x;
			for(int i=0;i<trainingOutput[0].length;i++)
				mes+="\t"+res.getValue(0,i);
			System.out.println(mes.replace('.',','));
		}

		System.out.println("weights : "+net.getWeights()); //a b c
		System.out.println("error :"+net.error);
	}
}

