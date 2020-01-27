public class Program { 
    
    @SecurityType( OwnerPrincipal="Simon", ReaderPrincipal="Simon, Peter" )
    private static int priavteA = 0;
    
    @SecurityType( OwnerPrincipal="Peter", ReaderPrincipal="Peter, Simon, Ondrej" )
	public static int publicC = 2;
	
	public static DalsiaKlasa referenceB;
	
	@interface SecurityType {
	     String OwnerPrincipal () default "";
	     String[] ReaderPrincipal() default "";
    }

    public static void main(String[] args) { 
		
		publicC = priavteA;

		@SecurityType( OwnerPrincipal="Simon", ReaderPrincipal="Simon, Peter" )
        int localWithType = 0;
		
		while(priavteA > 0){
			publicC += 3;
			localWithType = publicC;
		}
        
        int localVariable = priavteA + publicC;
        
		if(localVariable > 0){
			publicC++;
		}else{
			publicC--;
        }
        
        //tellSecret();
        
    }

	public static void tellSecret(){
		return priavteA;
    }
}