package service;

import java.util.Objects;

public class MainControllerFactory {

	public IMainController createMainController() {
		IMainController mc = new MainController();
		return mc ;
	}
}
