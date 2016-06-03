package AutomationLibTests;

import AutomationLibrary.Utilities.HTMLParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HTMLParserTest {
    String html = "<HTML>\n" +
            "<HEAD>\n" +
            "\t<META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=windows-1252\">\n" +
            "\t<TITLE></TITLE>\n" +
            "</HEAD>\n" +
            "<BODY LANG=\"en-NZ\" DIR=\"LTR\">\n" +
            "<P CLASS=\"wibble\" ID=\"wobble\">Text1</P>\n" +
            "<TABLE WIDTH=100% BORDER=1 CELLPADDING=4 CELLSPACING=3 STYLE=\"page-break-inside: avoid\">\n" +
            "\t<COL WIDTH=85*>\n" +
            "\t<COL WIDTH=85*>\n" +
            "\t<COL WIDTH=85*>\n" +
            "\t<TR VALIGN=TOP>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t</TR>\n" +
            "\t<TR VALIGN=TOP>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P>abc</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33% SDVAL=\"123\" SDNUM=\"5129;\">\n" +
            "\t\t\t<P>123</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t</TR>\n" +
            "\t<TR VALIGN=TOP>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t</TR>\n" +
            "\t<TR VALIGN=TOP>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t</TR>\n" +
            "\t<TR VALIGN=TOP>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t\t<TD WIDTH=33%>\n" +
            "\t\t\t<P><BR>\n" +
            "\t\t\t</P>\n" +
            "\t\t</TD>\n" +
            "\t</TR>\n" +
            "</TABLE>\n" +
            "<P><BR><BR>\n" +
            "</P>\n" +
            "</BODY>\n" +
            "</HTML>";


    @Test
    public void HTMLParserTest() {
        HTMLParser htmlParser = new HTMLParser(html);
        String value = htmlParser.getElementIDByClassName("wibble");
        assertEquals("wobble", value);
    }


}


