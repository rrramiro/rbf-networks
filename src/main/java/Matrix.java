
class Matrix {
	double matrix[][];
	//--------------------------------------------------------------------------
	public Matrix(int rows,int columns){
		matrix=new double[rows][columns];
		for(int i=0;i<this.matrix.length;i++)
			for(int j=0;j<this.matrix[i].length;j++)
				this.matrix[i][j]=0.0d;
	}
	public Matrix(Matrix that){
		this.matrix=new double[that.getRows()][that.getColumns()];
		for(int i=0;i<this.matrix.length;i++)
			for(int j=0;j<this.matrix[i].length;j++)
				this.matrix[i][j]=that.matrix[i][j];
	}
	public Matrix(int rows,int columns,Matrix that,int rowsOffset,int columnsOffset){
		matrix=new double[rows][columns];
		for(int i=0;i<this.matrix.length;i++)
			for(int j=0;j<this.matrix[i].length;j++)
				this.matrix[i][j]=that.matrix[(i+rowsOffset)%that.getRows()][(j+columnsOffset)%that.getColumns()];
	}
	public Matrix(double matrix[][]){
		this.matrix=matrix;
	}
	
	//--------------------------------------------------------------------------
	
	public void setValue(int r,int c,double val){
		this.matrix[r][c]=val;
	}
	public double getValue(int r,int c){
		return this.matrix[r][c];
	}
	public int getRows(){
		return matrix.length;
	}
	public int getColumns(){
		return matrix[0].length;
	}
	
	//--------------------------------------------------------------------------
	public static Matrix add(Matrix a,Matrix b){
		Matrix r=new Matrix(a);
		for(int i=0;i<r.matrix.length;i++)
			for(int j=0;j<r.matrix[i].length;j++)
				r.matrix[i][j]+=b.matrix[i][j];
		return r;
	}
	public static Matrix sub(Matrix a,Matrix b){
		Matrix r=new Matrix(a);
		for(int i=0;i<r.matrix.length;i++)
			for(int j=0;j<r.matrix[i].length;j++)
				r.matrix[i][j]-=b.matrix[i][j];
		return r;
	}

	public static Matrix multiply(Matrix a,Matrix b){
		Matrix r=new Matrix(a.getRows(),b.getColumns());
		int alpha=a.getColumns();//b.getRows();
		for(int i=0;i<r.matrix.length;i++){
			for(int j=0;j<r.matrix[i].length;j++){
				for(int k=0;k<alpha;k++){
					r.matrix[i][j]+=a.matrix[i][k]*b.matrix[k][j];
				}
			}
		}
		return r;
	}
	
	public static Matrix multiply(double u,Matrix a){
		Matrix r=new Matrix(a);
		for(int i=0;i<r.matrix.length;i++)
			for(int j=0;j<r.matrix[i].length;j++)
				r.matrix[i][j]*=u;
		return r;
	}
	public static Matrix com(Matrix a){
		if(a.matrix.length!=a.matrix[0].length)
			throw new RuntimeException("com: must be square matrix.");
		Matrix r=new Matrix(a.getRows(),a.getColumns());
		if(a.matrix.length==1){
			return a;
		}else if(a.matrix.length==2){
			r.matrix[0][0]=a.matrix[1][1];
			r.matrix[0][1]=-a.matrix[1][0];
			r.matrix[1][0]=-a.matrix[0][1];
			r.matrix[1][1]=a.matrix[0][0];
		}else{
			for(int i=0;i<r.matrix.length;i++)
				for(int j=0;j<r.matrix[i].length;j++)
					r.matrix[i][j]=(new Matrix(2,2,a,i+1,j+1)).det();
		}
		return r;
	}
	public static Matrix inverse(Matrix a){
		if(a.matrix.length!=a.matrix[0].length)
			throw new RuntimeException("inverse: must be square matrix.");
		double detA=a.det();
		if(detA==0.0)
			throw new RuntimeException("inverse: matrix can't be inverse.");
		Matrix r=Matrix.com(a);
		r.multiply(1.0d/detA);
		return r;
	}
	public static Matrix tanspose(Matrix a){
		Matrix r=new Matrix(a.getColumns(),a.getRows());
		for(int i=0;i<r.matrix.length;i++){
			for(int j=0;j<r.matrix[i].length;j++){
				r.matrix[i][j]=a.matrix[j][i];
			}
		}
		return r;
	}
	public static Matrix indentity(int size,int init){
		Matrix r=new Matrix(size,size);
		for(int i=0;i<size;i++)
			r.matrix[i][i]=init;
		return r;
	}
	
	public static Matrix indentity(int size){
		return Matrix.indentity(size,1);
	}
	
	public static double det(Matrix a){
		if(a.matrix.length!=a.matrix[0].length)
			throw new RuntimeException("det: must be square matrix.");
		double d=0.0;
		if(a.matrix.length==1){
			return a.matrix[0][0];
		}else if(a.matrix.length==2){
			return a.matrix[0][0]*a.matrix[1][1]-a.matrix[0][1]*a.matrix[1][0];
		}else if(a.matrix.length==3){
			return(a.matrix[0][0] * a.matrix[1][1] * a.matrix[2][2])
				+ (a.matrix[0][1] * a.matrix[1][2] * a.matrix[2][0])
				+ (a.matrix[0][2] * a.matrix[1][0] * a.matrix[2][1])
				- (a.matrix[0][2] * a.matrix[1][1] * a.matrix[2][0])
				- (a.matrix[1][2] * a.matrix[2][1] * a.matrix[0][0])
				- (a.matrix[2][2] * a.matrix[0][1] * a.matrix[1][0]);
		}else{
			for(int k=0;k<a.matrix.length;k++){
				if(a.matrix[k][0]!=0){
					d+=(1-2*(k%2))*a.matrix[k][0]*(new Matrix(a.getRows()-1,a.getColumns()-1,a,k+1,1)).det();
				}
			}
		}
		return d;
	}
	//--------------------------------------------------------------------------
	public Matrix add(Matrix b){
		return Matrix.add(this,b);
	}
	public Matrix sub(Matrix b){
		return Matrix.sub(this,b);
	}
	public Matrix multiply(Matrix b){
		return Matrix.multiply(this,b);
	}
	public Matrix multiply(double b){
		return Matrix.multiply(b,this);
	}
	public Matrix tanspose(){
		return Matrix.tanspose(this);
	}
	public Matrix inverse(){
		return Matrix.inverse(this);
	}
	public double det(){
		return Matrix.det(this);
	}
	public Matrix com(){
		return Matrix.com(this);
	}
	public String toString(){
		String s="{";
		for(int i=0;i<this.matrix.length;i++){
			s+="{";
			for(int j=0;j<this.matrix[i].length;j++){
				s+=this.matrix[i][j]+((j==this.matrix[i].length-1)?"":";");
			}
			s+="}";
		}
		s+="}";
		return s;
	}
}

