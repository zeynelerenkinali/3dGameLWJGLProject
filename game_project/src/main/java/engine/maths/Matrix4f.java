package engine.maths;

public class Matrix4f {
    public static final int SIZE = 4;
    private float[] elements = new float[SIZE * SIZE];

    public static Matrix4f identity(){ // base matrice that all matrices based on
        // 1000
        // 0100
        // 0010
        // 0001
        Matrix4f result = new Matrix4f();

        for(int i = 0; i  < SIZE; i++){
            for(int j = 0; j  < SIZE; j++){
                result.set(i, j, 0); // all the elements in the matrix set to zero.
            }
        }
        
        result.set(0, 0, 1);//x, y, value // set operation stands for 
        result.set(1, 1, 1);
        result.set(2, 2, 1);
        result.set(3, 3, 1);

        return result; // return new matrix
    }
    
    public static Matrix4f translate(Vector3f translate){
        // 100x
        // 010y
        // 001z
        // 0001
        Matrix4f result = Matrix4f.identity();
        result.set(3, 0, translate.getX());
        result.set(3, 1, translate.getY());
        result.set(3, 2, translate.getZ());
        return result;
    }

    public static Matrix4f rotate(float angle, Vector3f axis){
        Matrix4f result = Matrix4f.identity();

        float cos = (float) Math.cos(Math.toRadians(angle));
        float sin = (float) Math.sin(Math.toRadians(angle));
        float C = 1 - cos;

        // Create the matrix
		result.set(0, 0, cos + axis.getX() * axis.getX() * C);
		result.set(0, 1, axis.getX() * axis.getY() * C - axis.getZ() * sin);
		result.set(0, 2, axis.getX() * axis.getZ() * C + axis.getY() * sin);
		result.set(1, 0, axis.getY() * axis.getX() * C + axis.getZ() * sin);
		result.set(1, 1, cos + axis.getY() * axis.getY() * C);
		result.set(1, 2, axis.getY() * axis.getZ() * C - axis.getX() * sin);
		result.set(2, 0, axis.getZ() * axis.getX() * C - axis.getY() * sin);
		result.set(2, 1, axis.getZ() * axis.getY() * C + axis.getX() * sin);
		result.set(2, 2, cos + axis.getZ() * axis.getZ() * C);
        return result;
    }

    public static Matrix4f scale(Vector3f scalar){
    //    x000
    //    0y00
    //    00z0
    //    0001     
        Matrix4f result = Matrix4f.identity();
        result.set(0, 0, scalar.getX());
        result.set(1, 1, scalar.getY());
        result.set(2, 2, scalar.getZ());
        return result;
    }

    public static Matrix4f transform(Vector3f position, Vector3f rotation, Vector3f scale){
        Matrix4f result = Matrix4f.identity();

        Matrix4f translationMatrix = Matrix4f.translate(position);
        Matrix4f rotXMAtrix = Matrix4f.rotate(rotation.getX(), new Vector3f(1, 0, 0));
        Matrix4f rotYMAtrix = Matrix4f.rotate(rotation.getY(), new Vector3f(0, 1, 0));
        Matrix4f rotZMAtrix = Matrix4f.rotate(rotation.getZ(), new Vector3f(0, 0, 1));

        Matrix4f scaleMatrix = Matrix4f.scale(scale);

        Matrix4f rotationMatrix = Matrix4f.multiply(rotXMAtrix, Matrix4f.multiply(rotYMAtrix, rotZMAtrix));

        result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));

        return result;
    }

    public static Matrix4f projection(float fov, float aspect, float near, float far){
        Matrix4f result = Matrix4f.identity();

        float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
        float range = far - near;

        result.set(0, 0, 1.0f / (aspect*tanFOV));
        result.set(1, 1, 1.0f / tanFOV);
        result.set(2, 2, -((far + near) / range));
        result.set(2, 3, -1.0f);
        result.set(3, 2, -((2 * far * near) / range));
        result.set(3, 3, 0.0f);

        return result;
    }

    public static Matrix4f view(Vector3f position, Vector3f rotation){
        Matrix4f result = Matrix4f.identity();

        Vector3f negative = new Vector3f(-position.getX(), -position.getY(), -position.getZ()); 
        Matrix4f translationMatrix = Matrix4f.translate(negative);
        Matrix4f rotXMAtrix = Matrix4f.rotate(rotation.getX(), new Vector3f(1, 0, 0));
        Matrix4f rotYMAtrix = Matrix4f.rotate(rotation.getY(), new Vector3f(0, 1, 0));
        Matrix4f rotZMAtrix = Matrix4f.rotate(rotation.getZ(), new Vector3f(0, 0, 1));

        Matrix4f rotationMatrix = Matrix4f.multiply(rotZMAtrix, Matrix4f.multiply(rotYMAtrix, rotXMAtrix));

        result = Matrix4f.multiply(translationMatrix, rotationMatrix);

        return result;
    }

    public static Matrix4f multiply(Matrix4f matrix, Matrix4f other){
        Matrix4f result = Matrix4f.identity();

        for(int i = 0; i  < SIZE; i++){
            for(int j = 0; j  < SIZE; j++){
                result.set(i, j,    matrix.get(i, 0) * other.get(0, j) +
                                    matrix.get(i, 1) * other.get(1, j) +
                                    matrix.get(i, 2) * other.get(2, j) +
                                    matrix.get(i, 3) * other.get(3, j));
            }
        }
        return result;
    }



    public float get(int x, int y){
        return elements[y * SIZE + x]; // converts 2D numbers to 1D number.
    }

    public void set(int x, int y, float value){
        elements[y * SIZE + x] = value;
    }

    public float[] getAll(){
        return elements;
    }
}
