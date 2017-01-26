import java.util.*;


class RBFNetwork {
	Matrix weights;
	RBF[] functions;
	Matrix error;
	public RBFNetwork(double [][]i,double [][]o,RBF []functions){
		this(new Matrix(i),new Matrix(o),functions);
	}
	public RBFNetwork(Matrix input,Matrix output,RBF []functions){
		if(input.getRows()!=output.getRows())
			throw new RuntimeException("Dimension Error : ");
		this.functions=functions;
		Matrix H=new Matrix(input.getRows(),functions.length);
		for(int i=0;i<H.getRows();i++){
			Matrix temp=new Matrix(1,input.getColumns(),input,i,0);
			for(int j=0;j<H.getColumns();j++){
				H.setValue(i,j,functions[j].eval(temp));
			}
		}
		Matrix Ht=Matrix.tanspose(H);
		Matrix invA=Matrix.inverse(Matrix.multiply(Ht,H));
		weights=Matrix.multiply(Matrix.multiply(invA,Ht),output);
		
		Matrix Proj=Matrix.sub(Matrix.indentity(H.getRows()),Matrix.multiply(Matrix.multiply(H,invA),Ht));
		Matrix ProjSt=Matrix.tanspose(Matrix.multiply(Proj,Proj));
		error=Matrix.multiply(Matrix.multiply(output,ProjSt),output);
		
		
	}

	public Matrix getError(){
		return error;
	}
	public Matrix getWeights(){
		return weights;
	}

	public Matrix eval(Matrix input){
		Matrix result=new Matrix(1,weights.getColumns());
		for(int j=0;j<weights.getColumns();j++){
			double res=0.0;
			for(int i=0;i<weights.getRows();i++){
				res+=weights.getValue(i,j)*functions[i].eval(input);
			}
			result.setValue(0,j,res);
		}
		return result;
	}
}
/*

APC-III Algorithm
	Input: (x) training patterns {x(1),x(2),...,x(p)}
	Output: (c) centers of clusters
var
	C : number of clusters
	P : number of patterns
	x(j) : value of the j-th pattern
	c(j) : center of the j­th cluster
	n(j) : number of patterns in the j­th cluster
	d(i,j) : distance between x(i) and the j­th cluster
	R0: Constant radial
begin
	C = 1;
	c(1) <- x(1);
	n(1) := 1;
	for i := 2 to P do // for each pattern
		for j := 1 to C do // for each cluster
			//compute d(i,j);
			if d(i,j) <= R0 then
				// include x i into the j­th cluster
				c(j) <- (c(j)*n(j)+x(i))/(n(i)+1);
				n(i) := n(i) + 1;
				exit from the loop;
			end if
		end for
		if xi is not included in any clusters then
			// create a new cluster
			C := C+1;
			c(C) <- x(i);
			n(C) := 1;
		end if
	end for
end


compute R0
	Input: (x) training patterns {x(1),x(2),...,x(p)}
	Input: (alpha) reduction coeff
	Ouput : (R0) Constant radial
var
	P : number of patterns
	x(j) : value of the j-th pattern
	d(i,j) : distance between x(i) and x(j)
	min(i) : minimum distance between x(i) and x(j) for all j diffent to i
beginbetween
	for i=1 to P
	    min(i):=infinte
		for j=1 to P
			if i<>j  and  min(i)>d(i,j) then
				min(i) <- d(i,j);
			end if
		end for
		R0 <- R0+min(i);
	end for
	R0:=alpha*R0/P;
end

*/