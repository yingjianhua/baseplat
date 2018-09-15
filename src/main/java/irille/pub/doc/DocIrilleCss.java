package irille.pub.doc;

import irille.pub.html.Css;
import irille.pub.html.CssLine;

public class DocIrilleCss extends Css<DocIrilleCss> {
	public static final DocIrilleCss INST = new DocIrilleCss();
	public CssLine TALLY_ACC, TALLY_AMT, CR, EXAMPLE, QUESTION, ANSWER;
	public CssLine FRAME_TITLE_FONT, FRAME_HEADING_FONT, FRAME_ITEM_FONT;
	public CssLine TRAN, DEP, ACT, IMPORT, CODE, UPPER_NUMBER, PROPERTY,
	    PROPERTY_NAME, PROPERTY_NOTNULL, PROPERTY_EXPLAIN;

	@Override
	public void init() {
		setFileName(DocTran.CSS_FILE);
		TRAN = id("tran").MarginLeft(0).FontSize("1.4em")
		    .BackgroudColor("#9EB6E7").BorderTop("thin inset #564b47")
		    .BorderBottom("thin outset #564b47").Padding(7, 0, 1, 2).DisplayBlock()
		    .FontWeightBolder().Color("#FFFFFF").FontStyleItalic();
		DEP = id("dep").Margin("2em 0em 1em 0em").FontSize("1em")
		    .BackgroudColor("#9EB6E7").BorderTop("thin inset #564b47")
		    .BorderBottom("thin outset #564b47").Padding("0.3em 0em 0.05em 0em")
		    .DisplayBlock().FontWeightBolder();
		body().FontFamily("微软雅黑,宋体,courier new,courier").FontSize("small")
		    .Width("95%").LineHeight("1.3em");
		h1().FontSize("145%");
		ol().MarginTop(0).MarginRight(0).MarginBottom(0).MarginLeft("1.1em");
		li().MarginLeft("1.1em");
		ACT = clazz("act").FontSize("1.1em").Color("#000000").DisplayBlock()
		    .Padding("0.5em 0px 0.2em 0px").FontWeightBolder();
		IMPORT = clazz("import").FontStyleItalic().FontWeightBolder();
		CODE = clazz("code").BackgroudColor("#EEEEEE");
		UPPER_NUMBER = clazz("upperNumber").FontSize("+1").FontWeightBold();
		p().Margin(0);
		add("img-text").setParent("p").TextAlignCenter().DisplayBlock();
		add("img-text").TextAlignCenter().DisplayBlock().MarginTop(10)
		    .MarginBottom(10);
		add("img.textleft").TextAlignLeft().DisplayBlock().MarginTop(10)
		    .MarginBottom(10);
		add("table.tally").MarginLeft("3em").PaddingLeft("3em").PositionStatic()
		    .Left("330em").Clip("rect(auto,auto,auto,40px)")
		    .BorderCollapseCollapse();
		PROPERTY = clazz("property").setParent("table").Width("100%")
		    .BorderCollapseCollapse();
		PROPERTY_NAME = clazz("propertyName").setParent("td").Border("thin solid");
		// fixed:10em;
		PROPERTY_NOTNULL = clazz("propertyNotnull").setParent("td").Width("100%")
		    .BorderCollapseCollapse();
		PROPERTY_EXPLAIN = clazz("propertyExplain").setParent("td").Border(
		    "thin solid");
		add("th.propertyName").Width("10em");
		add("th.propertyNotnull").Width("3em");
		add("th.propertyExplain").Width("auot");
		table().BorderCollapseCollapse();
		th().Border("thin solid").BackgroudColor("#9EB6E7");
		td().Border("thin solid");
		TALLY_ACC = clazz("tallyAcc").setParent("td").Width("22em")
		    .TextIndent("1em");
		TALLY_AMT = clazz("tallyAmt").setParent("td").Width("10em")
		    .TextAlignRight();
		CR = clazz("cr").setParent("ul").TextIndent("1em").ListStyleTypeNone();
		EXAMPLE = clazz("example").BackgroudColor("#EEEEEE");
		QUESTION = clazz("question").BackgroudColor("lightblue").Color("darkred");
		ANSWER = clazz("answer").BackgroudColor("lightyellow").Color("#223311")
		    .LineHeight(21).FontSize(13);
		FRAME_TITLE_FONT = clazz("frameTitleFont").FontSize("100%")
		    .FontStyleItalic().FontWeightBold();
		FRAME_HEADING_FONT = clazz("frameHeadingFont").FontSize("90%");
		FRAME_ITEM_FONT = clazz("frameItemFont").FontSize("90%");
	}
}
