package weasel.compiler;

import java.util.ArrayList;
import java.util.List;

import weasel.interpreter.WeaselModifier;

public class WeaselModifierMap {
	
	public static String getModifierName(int modifier){
		List<String> output = new ArrayList<>();
		if((modifier & WeaselModifier.PRIVATE)!=0){
			output.add("private");
		}
		if((modifier & WeaselModifier.PUBLIC)!=0){
			output.add("public");
		}
		if((modifier & WeaselModifier.PROTECTED)!=0){
			output.add("protected");
		}
		if((modifier & WeaselModifier.STATIC)!=0){
			output.add("static");
		}
		if((modifier & WeaselModifier.ABSTRACT)!=0){
			output.add("abstract");
		}
		if((modifier & WeaselModifier.FINAL)!=0){
			output.add("final");
		}
		if((modifier & WeaselModifier.NATIVE)!=0){
			output.add("native");
		}
		if(output.isEmpty())
			return "";
		if(output.size()==1)
			return output.get(0);
		StringBuilder sOutput = new StringBuilder();
		for(int i=0; i<output.size()-2; i++){
			sOutput.append(output.get(i)).append(", ");
		}
		sOutput.append(output.get(output.size() - 2)).append(" & ");
		sOutput.append(output.get(output.size() - 1));
		return sOutput.toString();
	}
	
	public static int getModifier(String modifier){
		switch (modifier) {
			case "private":
				return WeaselModifier.PRIVATE;
			case "public":
				return WeaselModifier.PUBLIC;
			case "protected":
				return WeaselModifier.PROTECTED;
			case "final":
				return WeaselModifier.FINAL;
			case "native":
				return WeaselModifier.NATIVE;
			case "static":
				return WeaselModifier.STATIC;
			case "abstract":
				return WeaselModifier.ABSTRACT;
		}
		return 0;
	}
	
}
