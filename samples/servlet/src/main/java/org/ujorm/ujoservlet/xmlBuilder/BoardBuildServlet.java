/*
 * Copyright 2018-2018 Pavel Ponec, https://github.com/pponec
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ujorm.ujoservlet.xmlBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ujorm.tools.dom.HtmlElement;
import org.ujorm.tools.dom.XmlElement;
import org.ujorm.tools.xml.Html;
import org.ujorm.ujoservlet.tools.ApplService;
import org.ujorm.ujoservlet.tools.BoardModel;

@WebServlet(BoardBuildServlet.URL_PATTERN)
public class BoardBuildServlet extends HttpServlet {

    /** URL pattern */
    public static final String URL_PATTERN = "/boardBuidServlet";

    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(BoardBuildServlet.class.getName());

    /** Hidden data input */
    public static final String BOARD_PARAM = "board";

    /** Cell prefix */
    public static final String CELL_PREFIX_PARAM = "c";

    /** A reset action */
    public static final String RESET_ACTION = "reset";

    /* A common code page form request and response. Try the {@code  Charset.forName("windows-1250")} for example. */
    private final Charset charset = StandardCharsets.UTF_8;

    /** Show the first line of soufce code */
    public static final short SHOW_LINE = 69;

    /**
     * Handles the HTTP <code>GET</code> or <code>POST</code> method.
     * @param input A servlet request encoded by UTF_8
     * @param output A servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest input, HttpServletResponse output) throws ServletException, IOException {
        BoardModel boardModel = createBoardModel(input);

        HtmlElement html = buildHtmlElement("Drawing board", "css/board.css"); // A root of DOM model
        XmlElement form = buildFormElement(html.getBody(), boardModel);
        boardModel.getErrorMessage().ifPresent(msg -> form.addElement(Html.SPAN).addText(msg)); // Print an error message
        buildTheBoard(form, boardModel);
        buildResetButton(form);

        ApplService.addFooter(html.getBody(), this, SHOW_LINE);
        html.toResponse(output, true); // Render the result
    }

    /** Create new HTML element with a title and CSS style */
    private HtmlElement buildHtmlElement(String title, String css) {
        HtmlElement result = new HtmlElement(title, charset);
        result.addCssLink(css);
        result.addElementToBody(Html.H1).addText(title);
        return result;
    }

    /** Create a form element including a hidden fields */
    private XmlElement buildFormElement(final XmlElement parent, BoardModel board) {
        XmlElement result = parent.addElement(Html.FORM)
                .setAttrib(Html.A_METHOD, Html.V_GET);
        result.addElement(Html.INPUT)
                .setAttrib(Html.A_TYPE, Html.V_HIDDEN)
                .setAttrib(Html.A_NAME, BOARD_PARAM)
                .setAttrib(Html.A_VALUE, board.exportBoard());
        return result;
    }

    /** Create a clear button element */
    private void buildResetButton(XmlElement parent) {
        parent.addElement(Html.INPUT)
                .setAttrib(Html.A_CLASS, Html.V_RESET)
                .setAttrib(Html.A_TYPE, Html.V_SUBMIT)
                .setAttrib(Html.A_NAME, RESET_ACTION)
                .setAttrib(Html.A_VALUE, "Draw your own picture");
    }

    /** Create the boad element */
    protected void buildTheBoard(XmlElement parent, BoardModel board) {
        final XmlElement table = parent.addElement(Html.TABLE)
                .setAttrib(Html.A_CELLPADDING, 0)
                .setAttrib(Html.A_CELLSPACING, 0)
                .setAttrib(Html.A_CLASS, "board")
                ;
        for (int y = 0; y < board.getHeight(); y++) {
            final XmlElement rowElement = table.addElement(Html.TR);
            for (int x = 0; x < board.getWidth(); x++) {
                rowElement.addElement(Html.TD)
                        .setAttrib(Html.A_CLASS, board.isStone(x, y) ? "s" : null)
                        .addElement(Html.INPUT)
                        .setAttrib(Html.A_TYPE, Html.V_SUBMIT)
                        .setAttrib(Html.A_NAME, CELL_PREFIX_PARAM + (y * board.getWidth() + x))
                        .setAttrib(Html.A_VALUE, "");
            }
        }
    }

    /** Build a board */
    private BoardModel createBoardModel(HttpServletRequest input) throws UnsupportedEncodingException {
        input.setCharacterEncoding(charset.toString());

        int width = 21;
        int height = 9;

        BoardModel result;
        if (input.getParameter(RESET_ACTION) != null) {
            result = new BoardModel(width, height, null);
        } else try {
            result = new BoardModel(width, height, input.getParameter(BOARD_PARAM));
            final Enumeration<String> params = input.getParameterNames();
            while (params.hasMoreElements()) {
                final String param = params.nextElement();
                if (param.startsWith(CELL_PREFIX_PARAM)) {
                    int pointer = Integer.parseInt(param.substring(1));
                    result.setStone(pointer);
                    break;
                }
            }
        } catch (Exception e) {
            String msg = "An error processing parameters";
            LOGGER.log(Level.WARNING, msg, e);
            result = new BoardModel(width, height, null);
            result.setErrorMessage(msg);
        }
        return result;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param input servlet request
     * @param output servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest input, HttpServletResponse output) throws ServletException, IOException {
        doGet(input, output);
    }
}
