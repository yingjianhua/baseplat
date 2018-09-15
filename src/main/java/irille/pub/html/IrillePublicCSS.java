package irille.pub.html;


/**
 * irille公共Css文件
 * @author whx
 *
 */
public class IrillePublicCSS extends Css<IrillePublicCSS> {
	// 屏幕宽度
	public static final int WIDTH = 950;
	public static IrillePublicCSS PUBLIC_CSS = new IrillePublicCSS()
	    .setFileName("irille/webapp/css/layout.css");
	public CssLine SPACE_WARP, COLOR1, COLOR2, ACOLOR1, ACOLOR2, ACOLOR3,
	    ACOLOR4, ZCOLOR1, ZCOLOR2, NUM, ERROR_MSG, ERROR_MSG_STYLE, PANAL,
	    PANAL_L, PANAL_R, IMGAGE58_50, IMAGE73_65, IMAGE116_100, LINES_SPILE1,
	    LINE_SPILE3, LINE_SPACE4, LINE_SPACE8,LINE_SPACE16, TITLE1, SPILE1, SPILE2, SPILE3,
	    BUTTON1, BUTTON_SAVE1, HIDE;

	public static void main(String[] args) {
		System.err.println(PUBLIC_CSS.outCssCode("  "));
	}

	@Override
	public void init() {
	  super.init();
		body().FontFamily("微软雅黑,宋体,courier new,courier");

		SPACE_WARP = clazz("spacewrap").Width(WIDTH).MarginAuto()
		    .Backgroud("#f9f9f9");
		COLOR1 = clazz("color1").Color("#A62D0F").setChildren(
		    ".color1 a:link,.color1 a:visited,.color1 a:hover,.color1 a:active");
		COLOR2 = clazz("color2").Color("#b6784f").setChildren(
		    ".color2 a:link,.color2 a:visited,.color2 a:hover,.color2 a:active");
		ACOLOR1 = clazz("acolor1")
		    .Color("#A62D0F")
		    .setChildren(
		        ".acolor1 a:link,.acolor1 a:visited,.acolor1 a:hover,.acolor1 a:active");
		ACOLOR2 = clazz("acolor2")
		    .Color("#b6784f")
		    .setChildren(
		        ".acolor2 a:link,.acolor2 a:visited,.acolor2 a:hover,.acolor2 a:active");
		ACOLOR3 = clazz("acolor3")
		    .Color("#white")
		    .setChildren(
		        ".acolor3 a:link,.acolor3 a:visited,.acolor3 a:hover,.acolor3 a:active");
		ACOLOR4 = clazz("acolor4").Color("#a37b6f").setChildren(
		    ".acolor4 a:link,.acolor4 a:visited");
		add(".acolor4 a:hover,.acolor4 a:active").Color("#A62D0F");
		ZCOLOR1 = clazz("zcolor1").Color("#A62D0F");
		ZCOLOR2 = clazz("zcolor2").Color("#b6784f");
		add("a.unLink:link, a.unLink:visited,a.unLink:hover, a.unLink:active")
		    .Color("#c1c1c1");
		NUM = clazz("num").Color("#c1c1c1");
		ERROR_MSG = clazz("errorMsg").Backgroud("/images/wrong.jpg", "no-repeat")
		    .BackgroudPosition(5).Padding(5, 10, 5, 25).Border("1px solid #fe817f")
		    .BackgroudColor("#fff3f4").LineHeight(18).MarginLeft(10).Color("red")
		    .TextAlignCenter().FloatLeft().FontSize(12)._DisplayInline();
		ERROR_MSG_STYLE = clazz("errorMsgStyle").TextAlignCenter().Height(30)
		    .MarginBottom(5).MarginAuto().MarginTop(0).PaddingTop(0).MarginLeft(50);
		ul().Margin(0).Padding(0).ListStyleTypeNone();
		PANAL = clazz("panal").FloatLeft().Width("100%");
		PANAL_L = clazz("panal_l").FloatLeft();
		PANAL_R = clazz("panal_r").FloatRight();
		IMGAGE58_50 = clazz("imgage58_50").FloatLeft().Width(58).Height(58);
		img().setParent(IMGAGE58_50).FloatLeft().Width(50).Height(50).MarginLeft(4);
		IMAGE73_65 = clazz("image73_65").FloatLeft().Width(73).Height(73);
		img().setParent(IMAGE73_65).FloatLeft().Width(65).Height(65).MarginLeft(4);
		IMAGE116_100 = clazz("image116_100").FloatLeft().Width(116).Height(116);
		img().setParent(IMAGE116_100).FloatLeft().Width(100).Height(100)
		    .MarginLeft(8);
		LINES_SPILE1 = clazz("lineSpile1").Width(2).Height("100%").Backgroud("red")
		    .OverflowHidden();
		LINE_SPILE3 = clazz("lineSpace3").FloatLeft().Width(1).Height(25)
		    .Backgroud("#a37b6f").OverflowHidden();
		LINE_SPACE4 = clazz("lineSpace4").FloatLeft().Width("100%").Height(4)
		    .OverflowHidden();
		LINE_SPACE8 = clazz("lineSpace8").FloatLeft().Width("100%").Height(8)
		    .OverflowHidden();
		LINE_SPACE16 = clazz("lineSpace16").FloatLeft().Width("100%").Height(16)
    .OverflowHidden();
		TITLE1 = clazz("title1").Width("100%").FontSize(16).FontWeightBolder();
		SPILE1 = clazz("spile1").FloatLeft().Width("100%").Height(1)
		    .Backgroud("/space/images/spile3.jpg", "repeat-x").OverflowHidden();
		SPILE2 = clazz("spile2").FloatLeft().Width("100%").Height(1)
		    .Backgroud("#a37b6f").OverflowHidden();
		SPILE3 = clazz("spile3").FloatLeft().Width("100%").Height(1)
		    .Backgroud("#a37b6f").OverflowHidden();
		BUTTON1 = clazz("button1").FloatLeft().Width(51).Height(21)
		    .Backgroud("/images/button1.png", "no-repeat");
		BUTTON_SAVE1 = clazz("buttonSave1").Width(53).Height(24)
		    .Backgroud("/space/images/save.jpg", "no-repeat");
		HIDE = clazz("hide").DisplayNone();
	}
}
