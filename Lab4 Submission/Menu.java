
public class Menu {

	private String promptHeader;
	private String[] promptOptions;
	private promptReaction[] promptReactions;
	
	private Menu parent;
	
	Menu(String promptHeader, String[] promptOptions, promptReaction[] promptReactions) {
		this.promptHeader = promptHeader;
		this.promptOptions = promptOptions;
		this.promptReactions = promptReactions;
	}
	
	Menu(String promptHeader, String[] promptOptions, promptReaction[] promptReactions, Menu parent) {
		this(promptHeader, promptOptions, promptReactions);
		this.parent = parent;
	}
	
	// getters 
	public String getPrompt() {
		String out = 
				"----------------------------------------------------\n" + 
				promptHeader
				+ "\n----------------------------------------------------";
		
		for(int i = 0; i < promptOptions.length ; i++) {
			out = out + "\n" + (i+1) + " " + promptOptions[i];
		}
		out += "\n";
		return out;
	}
	
	public Menu getParent() {
		return parent;
	}
	
	// client utility
	public void handleResponse(int response) {
		

		System.out.println("\n\n");
		
		// check if response is in bounds
		if (response > promptReactions.length || response < 1) {
			System.out.println("that number is not in the menu. Please type a number that is in the menu.");
			return;
		}
		
		// act on response
		promptReactions[response -1].react();
	}
	
}
