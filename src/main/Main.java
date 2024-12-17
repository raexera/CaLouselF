package main;

import javafx.application.Application;
import views.auth.LoginView;

// Bug List:
/*
1. Reason decline tidak bisa pake special karakter, misal ' pada kata isn't
2. Seller tidak bisa liat offer list, data berhasil masuk, ke database jadi harusnya tidak ada masalah di user end
   Coba cek logic di seller view sama di offercontroller. [FIXED]
3. Ada error pas accept offer sama decline offer
 * */

public class Main{
    
	public static void main(String[] args) {
        
        Application.launch(LoginView.class, args);
    }

}