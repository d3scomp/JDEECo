package cz.cuni.mff.d3s.deeco.manager;

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
import org.eclipse.ui.console.MessageConsoleStream;

public class ConsolePrinter {

	public MessageConsoleStream openNewConsoleWindow(String consoleName) {
		try {
			IConsoleManager cm = ConsolePlugin.getDefault().getConsoleManager();
			MessageConsole console = new MessageConsole(consoleName, null);
		    cm.addConsoles(new IConsole[]{console});

		    IWorkbench wb = PlatformUI.getWorkbench();
		    IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		    IWorkbenchPage page = win.getActivePage();
		    String id = IConsoleConstants.ID_CONSOLE_VIEW;
		    
		    IConsoleView view = (IConsoleView) page.showView(id);
			view.display(console);
		    return console.newMessageStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 	    
	}
	
}
