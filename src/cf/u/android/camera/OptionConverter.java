package cf.u.android.camera;


import java.util.Locale;

import android.hardware.Camera;

public class OptionConverter {
	
	//public static final String EFFECT_CF_INVERT = "cf_invert";
	
	public enum HistogramMenu{
		On,
		Off,
	}
	
	public enum MenuMenu{
		Settings,
		Simple_Gallery,
		External_Galley,
		Free_Version,
		Plus_Version,
		Finish,
		About_App,
		Flatten,
	}
	
	public enum CF_Effect{
		none,
		cf_invert,
		cf_sepia,
		cf_grayscale,
		cf_brightness,
		cf_contrast_brightness,
		cf_fisheye,
		cf_tonel,
		cf_toy,
		cf_flip_v,
		cf_flip_h,
		cf_flip_hv,
		cf_mirror_left,
		cf_mirror_top,
		cf_mirror_right,
		cf_mirror_bottom,		
	}

	
	public static final String WhiteBalance(String str){
		
		Locale loc = Locale.getDefault();
		if( !Locale.JAPAN.equals(loc) && !Locale.JAPANESE.equals(loc) ){
			return str;
		}
		
		if( Camera.Parameters.WHITE_BALANCE_AUTO.equals(str) ){
			return "自動";
		}
		if( Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT.equals(str) ){
			return "曇天";
		}
		if( Camera.Parameters.WHITE_BALANCE_DAYLIGHT.equals(str) ){
			return "太陽光";
		}
		if( Camera.Parameters.WHITE_BALANCE_FLUORESCENT.equals(str) ){
			return "蛍光灯";
		}
		if( Camera.Parameters.WHITE_BALANCE_INCANDESCENT.equals(str) ){
			return "白熱電球";
		}		
		if( Camera.Parameters.WHITE_BALANCE_SHADE.equals(str) ){
			return "日陰";
		}		
		if( Camera.Parameters.WHITE_BALANCE_TWILIGHT.equals(str) ){
			return "夕暮れ";
		}
		if( Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT.equals(str) ){
			return "自動";
		}
		return str;
	}
	
	public static final String Effect(String str){
		
		Locale loc = Locale.getDefault();
		if( !Locale.JAPAN.equals(loc) && !Locale.JAPANESE.equals(loc) ){
			return str;
		}
		
		if( Camera.Parameters.EFFECT_AQUA.equals(str) ){
			return "アクア";
		}
		if( Camera.Parameters.EFFECT_BLACKBOARD.equals(str) ){
			return "黒板";
		}
		if( Camera.Parameters.EFFECT_MONO.equals(str) ){
			return "モノクロ";
		}
		if( Camera.Parameters.EFFECT_NEGATIVE.equals(str) ){
			return "反転";
		}
		if( Camera.Parameters.EFFECT_NONE.equals(str) ){
			return "無し";
		}
		if( Camera.Parameters.EFFECT_POSTERIZE.equals(str) ){
			return "ポタリゼーション";
		}
		if( Camera.Parameters.EFFECT_SEPIA.equals(str) ){
			return "セピア";
		}
		if( Camera.Parameters.EFFECT_SOLARIZE.equals(str) ){
			return "露出";
		}		
		if( Camera.Parameters.EFFECT_WHITEBOARD.equals(str) ){
			return "ホワイトボード";
		}
	
		return str;
	}
	
	public static final String Scene(String str){
		
		Locale loc = Locale.getDefault();
		if( !Locale.JAPAN.equals(loc) && !Locale.JAPANESE.equals(loc) ){
			return str;
		}
		
		if( Camera.Parameters.SCENE_MODE_ACTION.equals(str) ){
			return "アクション";
		}
		if( Camera.Parameters.SCENE_MODE_AUTO.equals(str) ){
			return "自動";
		}
		if( Camera.Parameters.SCENE_MODE_BARCODE.equals(str) ){
			return "バーコード";
		}
		if( Camera.Parameters.SCENE_MODE_BEACH.equals(str) ){
			return "ビーチ";
		}
		if( Camera.Parameters.SCENE_MODE_CANDLELIGHT.equals(str) ){
			return "キャンドルナイト";
		}
		if( Camera.Parameters.SCENE_MODE_FIREWORKS.equals(str) ){
			return "花火";
		}
		if( Camera.Parameters.SCENE_MODE_LANDSCAPE.equals(str) ){
			return "背景";
		}
		if( Camera.Parameters.SCENE_MODE_NIGHT.equals(str) ){
			return "夜";
		}
		if( Camera.Parameters.SCENE_MODE_NIGHT_PORTRAIT.equals(str) ){
			return "夜と人物";
		}
		if( Camera.Parameters.SCENE_MODE_PARTY.equals(str) ){
			return "パーティー";
		}		
		if( Camera.Parameters.SCENE_MODE_PORTRAIT.equals(str) ){
			return "人物";
		}
		if( Camera.Parameters.SCENE_MODE_SNOW.equals(str) ){
			return "雪";
		}
		if( Camera.Parameters.SCENE_MODE_SPORTS.equals(str) ){
			return "スポーツ";
		}
		if( Camera.Parameters.SCENE_MODE_STEADYPHOTO.equals(str) ){
			return "握手";
		}
		if( Camera.Parameters.SCENE_MODE_SUNSET.equals(str) ){
			return "夕焼け";
		}
		if( Camera.Parameters.SCENE_MODE_THEATRE.equals(str) ){
			return "映画";
		}
		if( "portrait-illumi".equals(str) ){
			return "人とイルミ";
		}
		if( "illumi".equals(str) ){
			return "イルミネーション";
		}
		if( "backlight".equals(str) ){
			return "バックライト";
		}
		if( "pet".equals(str) ){
			return "ペット";
		}
		if( "cooking".equals(str) ){
			return "料理";
		}
		if( "off".equals(str) ){
			return "オフ";
		}
		
	
		return str;
	}
	
	
}
