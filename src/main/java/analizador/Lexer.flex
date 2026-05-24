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
        TablaGlobal.tokens.add(new Token(lexema, nombre, yyline + 1, yycolumn + 1));
    }
%}

/* MACROS */
LETRA   = [a-zA-ZáéíóúÁÉÍÓÚñÑ]
DIGITO  = [0-9]
ID      = {LETRA}({LETRA}|{DIGITO}|_)*
ENTERO  = {DIGITO}+
DECIMAL = {DIGITO}+ "." {DIGITO}+

/* ESPACIOS */
ESPACIOS = [ \t\r\n\f\u00A0]+

/* ── FIX: consumir TODO hasta el fin de línea incluyendo \n ── */
COMENTARIO_LINEA  = "//" [^\r\n]* (\r|\n|\r\n)?
COMENTARIO_BLOQUE = "/*" [^*] ~"*/" | "/*" "*"+ "/"

%%

<YYINITIAL> {

    {ESPACIOS}          { /* Ignorar */ }
    {COMENTARIO_LINEA}  { /* Ignorar */ }
    {COMENTARIO_BLOQUE} { /* Ignorar */ }

    /* Palabras Reservadas Estructura */
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

    /* Strings */
    "Relato" {
        agregarToken(yytext(), sym.RELATO);
        yybegin(STRING_MODE);
        return symbol(sym.RELATO, yytext());
    }

    /* Literales */
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
        }
    }

    [^] {
        TablaGlobal.errores.add(new modelos.ErrorCompilador(
            "Léxico", "Símbolo no reconocido: " + yytext(), yyline + 1));
    }
}

<STRING_MODE> {
    "Relato" {
        yybegin(YYINITIAL);
        agregarToken(yytext(), sym.RELATO);
        return symbol(sym.RELATO, yytext());
    }

    [\n\r] {
        yybegin(YYINITIAL);
    }

    [^R\n\r]+ {
        agregarToken(yytext(), sym.TEXTO_CONTENIDO);
        return symbol(sym.TEXTO_CONTENIDO, yytext());
    }

    "R" {
        agregarToken(yytext(), sym.TEXTO_CONTENIDO);
        return symbol(sym.TEXTO_CONTENIDO, yytext());
    }
}
