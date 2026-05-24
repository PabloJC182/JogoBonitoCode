package analizador;

import java_cup.runtime.Symbol;
import modelos.TablaGlobal;
import modelos.Token;
import config.ConfigLenguaje;

%%

%class Lexer
%cup
%public
%line
%column
%unicode

%state STRING_MODE

%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }

    private void agregarToken(String lexema, int symId){
        String nombre = ConfigLenguaje.getNombreTokenDesdeSym(symId);
<<<<<<< HEAD
        TablaGlobal.tokens.add(new Token(lexema, nombre, yyline + 1, yycolumn + 1));
=======
        TablaGlobal.tokens.add(new Token(lexema, nombre, yyline + 1, yycolumn +1));
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172
    }
%}

/* MACROS */
<<<<<<< HEAD
LETRA   = [a-zA-ZáéíóúÁÉÍÓÚñÑ]
DIGITO  = [0-9]
ID      = {LETRA}({LETRA}|{DIGITO}|_)*
ENTERO  = {DIGITO}+
DECIMAL = {DIGITO}+ "." {DIGITO}+

/* ESPACIOS */
ESPACIOS = [ \t\r\n\f\u00A0]+

/* ── FIX: consumir TODO hasta el fin de línea incluyendo \n ── */
COMENTARIO_LINEA  = "//" [^\r\n]* (\r|\n|\r\n)?
=======
LETRA = [a-zA-ZáéíóúÁÉÍÓÚñÑ]
DIGITO = [0-9]
ID = {LETRA}({LETRA}|{DIGITO}|_)*
ENTERO = {DIGITO}+
DECIMAL = {DIGITO}+ "." {DIGITO}+

/* ESPACIOS REFORZADOS */
ESPACIOS = [ \t\r\n\f\u00A0]+

COMENTARIO_LINEA = "//".*
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172
COMENTARIO_BLOQUE = "/*" [^*] ~"*/" | "/*" "*"+ "/"

%%

<YYINITIAL> {

    {ESPACIOS}          { /* Ignorar */ }
    {COMENTARIO_LINEA}  { /* Ignorar */ }
    {COMENTARIO_BLOQUE} { /* Ignorar */ }

    /* Palabras Reservadas Estructura */
<<<<<<< HEAD
    "IniciarPartido"   { agregarToken(yytext(), sym.IniciarPartido);  return symbol(sym.IniciarPartido,  yytext()); }
    "InicioPartido"    { agregarToken(yytext(), sym.InicioPartido);   return symbol(sym.InicioPartido,   yytext()); }
    "FinPartido"       { agregarToken(yytext(), sym.FinPartido);      return symbol(sym.FinPartido,      yytext()); }
    "FinJugada"        { agregarToken(yytext(), sym.FinJugada);       return symbol(sym.FinJugada,       yytext()); }
    "Local"            { agregarToken(yytext(), sym.Local);           return symbol(sym.Local,           yytext()); }
    "Visitante"        { agregarToken(yytext(), sym.Visitante);       return symbol(sym.Visitante,       yytext()); }
    "Ficha"            { agregarToken(yytext(), sym.Ficha);           return symbol(sym.Ficha,           yytext()); }
    "Narrar"           { agregarToken(yytext(), sym.Narrar);          return symbol(sym.Narrar,          yytext()); }
    "Liga"             { agregarToken(yytext(), sym.Liga);            return symbol(sym.Liga,            yytext()); }
    "Revancha"         { agregarToken(yytext(), sym.Revancha);        return symbol(sym.Revancha,        yytext()); }
    "Copa"             { agregarToken(yytext(), sym.Copa);            return symbol(sym.Copa,            yytext()); }
    "Entrenamiento"    { agregarToken(yytext(), sym.Entrenamiento);   return symbol(sym.Entrenamiento,   yytext()); }
    "Pretemporada"     { agregarToken(yytext(), sym.Pretemporada);    return symbol(sym.Pretemporada,    yytext()); }
    "Temporada"        { agregarToken(yytext(), sym.Temporada);       return symbol(sym.Temporada,       yytext()); }
    "ResultadoFinal"   { agregarToken(yytext(), sym.ResultadoFinal);  return symbol(sym.ResultadoFinal,  yytext()); }

    /* Tipos de Datos */
    "Dorsal"           { agregarToken(yytext(), sym.Dorsal);          return symbol(sym.Dorsal,          yytext()); }
    "Decimal"          { agregarToken(yytext(), sym.Decimal);         return symbol(sym.Decimal,         yytext()); }
    "JugadaTexto"      { agregarToken(yytext(), sym.JugadaTexto);     return symbol(sym.JugadaTexto,     yytext()); }

    /* Operadores */
    "Gana"             { agregarToken(yytext(), sym.Gana);            return symbol(sym.Gana,            yytext()); }
    "Pierde"           { agregarToken(yytext(), sym.Pierde);          return symbol(sym.Pierde,          yytext()); }
    "Empata"           { agregarToken(yytext(), sym.Empata);          return symbol(sym.Empata,          yytext()); }
    "GolSuma"          { agregarToken(yytext(), sym.GolSuma);         return symbol(sym.GolSuma,         yytext()); }
    "Contraataque"     { agregarToken(yytext(), sym.Contraataque);    return symbol(sym.Contraataque,    yytext()); }
=======
    "IniciarPartido"   { agregarToken(yytext(), sym.IniciarPartido); return symbol(sym.IniciarPartido, yytext()); }
    "InicioPartido"    { agregarToken(yytext(), sym.InicioPartido); return symbol(sym.InicioPartido, yytext()); }
    "FinPartido"       { agregarToken(yytext(), sym.FinPartido); return symbol(sym.FinPartido, yytext()); }
    "FinJugada"        { agregarToken(yytext(), sym.FinJugada); return symbol(sym.FinJugada, yytext()); }
    "Local"            { agregarToken(yytext(), sym.Local); return symbol(sym.Local, yytext()); }
    "Visitante"        { agregarToken(yytext(), sym.Visitante); return symbol(sym.Visitante, yytext()); }
    "Ficha"            { agregarToken(yytext(), sym.Ficha); return symbol(sym.Ficha, yytext()); }
    "Narrar"           { agregarToken(yytext(), sym.Narrar); return symbol(sym.Narrar, yytext()); }
    "Liga"             { agregarToken(yytext(), sym.Liga); return symbol(sym.Liga, yytext()); }
    "Revancha"         { agregarToken(yytext(), sym.Revancha); return symbol(sym.Revancha, yytext()); }
    "Copa"             { agregarToken(yytext(), sym.Copa); return symbol(sym.Copa, yytext()); }
    "Entrenamiento"    { agregarToken(yytext(), sym.Entrenamiento); return symbol(sym.Entrenamiento, yytext()); }
    "Pretemporada"     { agregarToken(yytext(), sym.Pretemporada); return symbol(sym.Pretemporada, yytext()); }
    "Temporada"        { agregarToken(yytext(), sym.Temporada); return symbol(sym.Temporada, yytext()); }
    "ResultadoFinal"   { agregarToken(yytext(), sym.ResultadoFinal); return symbol(sym.ResultadoFinal, yytext()); }

    /* Tipos de Datos */
    "Dorsal"           { agregarToken(yytext(), sym.Dorsal); return symbol(sym.Dorsal, yytext()); }
    "Decimal"          { agregarToken(yytext(), sym.Decimal); return symbol(sym.Decimal, yytext()); }
    "JugadaTexto"      { agregarToken(yytext(), sym.JugadaTexto); return symbol(sym.JugadaTexto, yytext()); }

    /* Operadores */
    "Gana"             { agregarToken(yytext(), sym.Gana); return symbol(sym.Gana, yytext()); }
    "Pierde"           { agregarToken(yytext(), sym.Pierde); return symbol(sym.Pierde, yytext()); }
    "Empata"           { agregarToken(yytext(), sym.Empata); return symbol(sym.Empata, yytext()); }
    "GolSuma"          { agregarToken(yytext(), sym.GolSuma); return symbol(sym.GolSuma, yytext()); }
    "Contraataque"     { agregarToken(yytext(), sym.Contraataque); return symbol(sym.Contraataque, yytext()); }
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172

    /* Strings */
    "Relato" {
        agregarToken(yytext(), sym.RELATO);
        yybegin(STRING_MODE);
        return symbol(sym.RELATO, yytext());
    }

    /* Literales */
<<<<<<< HEAD
    {DECIMAL} { agregarToken(yytext(), sym.DECIMAL_LITERAL); return symbol(sym.DECIMAL_LITERAL, Double.parseDouble(yytext())); }
    {ENTERO}  { agregarToken(yytext(), sym.ENTERO);          return symbol(sym.ENTERO,          Integer.parseInt(yytext())); }

    {ID} {
        Integer token = ConfigLenguaje.tokenMap.get(yytext());
        if (token != null) {
            agregarToken(yytext(), token);
            return symbol(token, yytext());
        } else {
            agregarToken(yytext(), sym.IDENTIFICADOR);
            return symbol(sym.IDENTIFICADOR, yytext());
=======
    {DECIMAL} { agregarToken(yytext(), sym.DECIMAL_LITERAL); return symbol(sym.DECIMAL_LITERAL, yytext()); }
    {ENTERO}  { agregarToken(yytext(), sym.ENTERO); return symbol(sym.ENTERO, yytext()); }

    {ID} {
        Integer token = ConfigLenguaje.tokenMap.get(yytext());
        if(token != null){
            agregarToken(yytext(), token); return symbol(token, yytext());
        } else {
            agregarToken(yytext(), sym.IDENTIFICADOR); return symbol(sym.IDENTIFICADOR, yytext());
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172
        }
    }

    [^] {
<<<<<<< HEAD
        TablaGlobal.errores.add(new modelos.ErrorCompilador(
            "Léxico", "Símbolo no reconocido: " + yytext(), yyline + 1));
=======
        TablaGlobal.errores.add(new modelos.ErrorCompilador("Léxico", "Símbolo no reconocido: " + yytext(), yyline + 1));
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172
    }
}

<STRING_MODE> {
    "Relato" {
        yybegin(YYINITIAL);
        agregarToken(yytext(), sym.RELATO);
        return symbol(sym.RELATO, yytext());
    }

<<<<<<< HEAD
=======
    /* Candado 1: Si hay un salto de línea, abortamos para no comernos el archivo */
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172
    [\n\r] {
        yybegin(YYINITIAL);
    }

<<<<<<< HEAD
=======
    /* Candado 2: Captura todo lo que NO sea una 'R' mayúscula ni salto de línea */
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172
    [^R\n\r]+ {
        agregarToken(yytext(), sym.TEXTO_CONTENIDO);
        return symbol(sym.TEXTO_CONTENIDO, yytext());
    }

<<<<<<< HEAD
=======
    /* Candado 3: Si hay una 'R' sola (ej: "Raro"), la tomamos como texto normal */
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172
    "R" {
        agregarToken(yytext(), sym.TEXTO_CONTENIDO);
        return symbol(sym.TEXTO_CONTENIDO, yytext());
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7811fd0017ca6c222942e7725fc8eb3d63c5a172
