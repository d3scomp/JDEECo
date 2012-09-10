package cz.cuni.mff.d3s.deeco.manager;

import java.io.PrintStream;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;

public class ConsolePrinter {
	
	private static final String CONSOLE_ID = "JDEECo SDE Tool Console";
	
	private MessageConsole console;

	public PrintStream openConsoleWindow() {
		try {
			IConsoleManager cm = ConsolePlugin.getDefault().getConsoleManager();
			if (console == null) {
				console = new MessageConsole(CONSOLE_ID, null);
			    cm.addConsoles(new IConsole[]{console});
			}
			console.activate();
		    return new PrintStream(console.newMessageStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 	    
	}
	
}
