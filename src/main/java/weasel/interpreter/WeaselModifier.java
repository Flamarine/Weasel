package weasel.interpreter;


public interface WeaselModifier {

	int PRIVATE=1;
	int PUBLIC=2;
	int PROTECTED=4;
	int FINAL=8;
	int NATIVE=16;
	int STATIC=32;
	int ABSTRACT=64;
	int INTERFACE = 128;
	
	int getModifier();
	
}
