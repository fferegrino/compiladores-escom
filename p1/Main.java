package LectorC;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        //<editor-fold desc="Declaramos nuestras expresiones regulares en el orden que las deseamos" defaultstate="collapsed">
        String[][] regexes = {{
                "񣄥�2�3�4�5�6�7�8�9粏~玜嘼嘽嘾噀噁噂噃噄噅噆噇噈噉噊噋噏噐噑噒噓噕噖噚噛噝嘇嘊嘋嘍嘐嘑嘒嘓嘔嘕嘖嘗嘙嘚嘜嘝嘠嘡嘢嘥嘦嘨嘩嘪嘫嘮嘷粏~玜嘼嘽嘾噀噁噂噃噄噅噆噇噈噉噊噋噏噐噑噒噓噕噖噚噛噝嘇嘊嘋嘍嘐嘑嘒嘓嘔嘕嘖嘗嘙嘚嘜嘝嘠嘡嘢嘥嘦嘨嘩嘪嘫嘮嘷�0�1�2�3�4�5�6�7�8�9互"
                ,"񣄥�2�3�4�5�6�7�8�9粏~�.~񣄥�2�3�4�5�6�7�8�9粏�"
                ,"񣄥�2�3�4�5�6�7�8�9粏"
                , "玬~a~i~n粐玤~o~t~o粐玶~e~t~u~r~n粐玸~i~g~n~e~d粐玸~i~z~e~o~f粐玹~y~p~e~d~e~f粐玼~n~i~o~n粐玼~n~s~i~g~n~e~d�"
                , "玣~o~r粐玝~r~e~a~k粐玞~o~n~t~i~n~u~e粐玠~o粐玾~h~i~l~e�"
                , "玸~w~i~t~c~h粐玞~a~s~e粐玡~l~s~e粐玦~f粐玠~e~f~a~u~l~t�"
                , "玞~h~a~r粐玦~n~t粐玪~o~n~g粐玸~h~o~r~t粐玣~l~o~a~t粐玠~o~u~b~l~e粐玽~o~i~d�" 
                ,"玜嘼嘽嘾噀噁噂噃噄噅噆噇噈噉噊噋噏噐噑噒噓噕噖噚噛噝嘇嘊嘋嘍嘐嘑嘒嘓嘔嘕嘖嘗嘙嘚嘜嘝嘠嘡嘢嘥嘦嘨嘩嘪嘫嘮嘷粏~玜嘼嘽嘾噀噁噂噃噄噅噆噇噈噉噊噋噏噐噑噒噓噕噖噚噛噝嘇嘊嘋嘍嘐嘑嘒嘓嘔嘕嘖嘗嘙嘚嘜嘝嘠嘡嘢嘥嘦嘨嘩嘪嘫嘮嘷�0�1�2�3�4�5�6�7�8�9互"
                ,"%~玠噄噓噊噚嘪噁嘑噀嘐噂嘒嘺嘇嘽噑噋噉�%�"
                , "�+~+粐�-~-粐�+�-�*�/�%粇�=�"
                , "�+�-�*�/�%�"
                , "<�>嚝>~=粐�<~=粐�!~=粐�=~=�"
                , "="
                , "!嚝&~&粐珅~|�"
                , "{噠嘯嘳�(�)�;�,�."
            }, {
                "ERROR"
                ,"NUMERO FLOTANTE"
                ,"NUMERO ENTERO"
                ,"PALABRA RESERVADA"
                ,"ESTRUCTURA REPETITIVA"
                ,"ESTRUCTURA SELECTIVA"
                ,"TIPO DE DATO" 
                ,"IDENTIFICADOR"
                ,"ESPECIFICADOR DE FORMATO"
                ,"OPERADOR INCREMENTO"
                ,"OPERADOR ARITMETICO"
                ,"OPERADOR RELACIONAL"
                ,"OPERADOR ASIGNACION"
                ,"OPERADOR LOGICO"
                ,"CARACTER ESPECIAL"
            },{
                "NO"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
                ,"OK"
            }};
        //</editor-fold>
        //<editor-fold desc="Creacion de los automatas" defaultstate="collapsed">
        int numero_de_automatas = regexes[0].length;
        int ix = 0;
        AutomataFD[] automatas = new AutomataFD[regexes[0].length];
        Regex2automaton r = new Regex2automaton();
        for (ix = 0; ix < numero_de_automatas; ix++) {
            //(log(""+ ix);
            automatas[ix] = r.automataDeExpresionRegular(regexes[0][ix], regexes[1][ix],regexes[2][ix]);
        }
        //</editor-fold>
        
        // Leemos las lineas de nuestro archivo
        Lector l;
        ArrayList<Linea> ls = null;
        // Declaramos los arreglos en los que guardaremos los lexemas (componentes y errores)
        ArrayList<Simbolo> simbolos = new ArrayList<>();
        ArrayList<ErrorLexico> errores = new ArrayList<>();
        
        
        try {
            l = new Lector(args[0]);
            ls = l.read();
        } catch (FileNotFoundException ex) {
            System.err.println("Hubo un error tratando de encontrar tu archivo");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Hubo una excepci髇: " + ex.getMessage());
            System.exit(1);
        }
        for (Linea ll : ls) {
            if (!ll.isVacia()) {
                for (String s : ll.getSimbolos()) {
                    String cadenaATrabajar = new String(s);
                    for (ix = 0; ix < numero_de_automatas;) {
                        AutomataFD evaluando = automatas[ix];
                        String cEvaluada = evaluando.test(cadenaATrabajar);
                        if (cEvaluada != null) {
                            if(evaluando.isError()){
                                errores.add(new ErrorLexico(ll.getNumeroDeLinea(), cEvaluada));
                            }else{
                            simbolos.add(new Simbolo(evaluando.getNombre(), cEvaluada, null, ll.getNumeroDeLinea()));
                            }
                            if (cEvaluada.length() == cadenaATrabajar.length()) {
                                break;
                            }
                            cadenaATrabajar = cadenaATrabajar.substring(cEvaluada.length());
                            ix = 0;
                            continue;
                        }else if(ix == (numero_de_automatas - 1)){
                            // Llegamos al ultimo automata
                            String no_reconocido = cadenaATrabajar.substring(0,1);
                            errores.add(new ErrorLexico(ll.getNumeroDeLinea(), no_reconocido));
                            if(cadenaATrabajar.length() == 1){
                                break;
                            }
                            cadenaATrabajar = cadenaATrabajar.substring(1);
                            ix = 0;
                            continue;
                        }
                        ix++;
                    }
                }
            }
        }
        try {
            imprime(errores, simbolos,args[2], args[1]);
        } catch (IOException ex) {
            System.err.println("Hubo una excepci髇: " + ex.getMessage());
            System.exit(1);
        }
    }
    
    
    private static void imprime(ArrayList<ErrorLexico> errores, ArrayList<Simbolo> simbolos, String err, String sim) 
            throws IOException{
        FileWriter fw;
            PrintWriter pw;
                // Escritura archivo
                fw = new FileWriter(err);
                pw = new PrintWriter(fw);
                pw.println("Linea del error\t\tSimbolo");
                for(ErrorLexico el : errores){
                pw.println(el.toString());}
                fw.close();
                
                // Escritura archivo
                fw = new FileWriter(sim);
                pw = new PrintWriter(fw);
                pw.println("Componente lexico\t\tLexema\t\t\tValor");
                for(Simbolo s : simbolos){
                pw.println(s.toString());}
                fw.close();
    }

    private static void log(String m) {
        System.out.println(m);
    }
}
