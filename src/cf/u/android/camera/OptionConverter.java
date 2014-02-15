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
			return "����";
		}
		if( Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT.equals(str) ){
			return "�ܓV";
		}
		if( Camera.Parameters.WHITE_BALANCE_DAYLIGHT.equals(str) ){
			return "���z��";
		}
		if( Camera.Parameters.WHITE_BALANCE_FLUORESCENT.equals(str) ){
			return "�u����";
		}
		if( Camera.Parameters.WHITE_BALANCE_INCANDESCENT.equals(str) ){
			return "���M�d��";
		}		
		if( Camera.Parameters.WHITE_BALANCE_SHADE.equals(str) ){
			return "���A";
		}		
		if( Camera.Parameters.WHITE_BALANCE_TWILIGHT.equals(str) ){
			return "�[���";
		}
		if( Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT.equals(str) ){
			return "����";
		}
		return str;
	}
	
	public static final String Effect(String str){
		
		Locale loc = Locale.getDefault();
		if( !Locale.JAPAN.equals(loc) && !Locale.JAPANESE.equals(loc) ){
			return str;
		}
		
		if( Camera.Parameters.EFFECT_AQUA.equals(str) ){
			return "�A�N�A";
		}
		if( Camera.Parameters.EFFECT_BLACKBOARD.equals(str) ){
			return "����";
		}
		if( Camera.Parameters.EFFECT_MONO.equals(str) ){
			return "���m�N��";
		}
		if( Camera.Parameters.EFFECT_NEGATIVE.equals(str) ){
			return "���]";
		}
		if( Camera.Parameters.EFFECT_NONE.equals(str) ){
			return "����";
		}
		if( Camera.Parameters.EFFECT_POSTERIZE.equals(str) ){
			return "�|�^���[�[�V����";
		}
		if( Camera.Parameters.EFFECT_SEPIA.equals(str) ){
			return "�Z�s�A";
		}
		if( Camera.Parameters.EFFECT_SOLARIZE.equals(str) ){
			return "�I�o";
		}		
		if( Camera.Parameters.EFFECT_WHITEBOARD.equals(str) ){
			return "�z���C�g�{�[�h";
		}
	
		return str;
	}
	
	public static final String Scene(String str){
		
		Locale loc = Locale.getDefault();
		if( !Locale.JAPAN.equals(loc) && !Locale.JAPANESE.equals(loc) ){
			return str;
		}
		
		if( Camera.Parameters.SCENE_MODE_ACTION.equals(str) ){
			return "�A�N�V����";
		}
		if( Camera.Parameters.SCENE_MODE_AUTO.equals(str) ){
			return "����";
		}
		if( Camera.Parameters.SCENE_MODE_BARCODE.equals(str) ){
			return "�o�[�R�[�h";
		}
		if( Camera.Parameters.SCENE_MODE_BEACH.equals(str) ){
			return "�r�[�`";
		}
		if( Camera.Parameters.SCENE_MODE_CANDLELIGHT.equals(str) ){
			return "�L�����h���i�C�g";
		}
		if( Camera.Parameters.SCENE_MODE_FIREWORKS.equals(str) ){
			return "�ԉ�";
		}
		if( Camera.Parameters.SCENE_MODE_LANDSCAPE.equals(str) ){
			return "�w�i";
		}
		if( Camera.Parameters.SCENE_MODE_NIGHT.equals(str) ){
			return "��";
		}
		if( Camera.Parameters.SCENE_MODE_NIGHT_PORTRAIT.equals(str) ){
			return "��Ɛl��";
		}
		if( Camera.Parameters.SCENE_MODE_PARTY.equals(str) ){
			return "�p�[�e�B�[";
		}		
		if( Camera.Parameters.SCENE_MODE_PORTRAIT.equals(str) ){
			return "�l��";
		}
		if( Camera.Parameters.SCENE_MODE_SNOW.equals(str) ){
			return "��";
		}
		if( Camera.Parameters.SCENE_MODE_SPORTS.equals(str) ){
			return "�X�|�[�c";
		}
		if( Camera.Parameters.SCENE_MODE_STEADYPHOTO.equals(str) ){
			return "����";
		}
		if( Camera.Parameters.SCENE_MODE_SUNSET.equals(str) ){
			return "�[�Ă�";
		}
		if( Camera.Parameters.SCENE_MODE_THEATRE.equals(str) ){
			return "�f��";
		}
		if( "portrait-illumi".equals(str) ){
			return "�l�ƃC���~";
		}
		if( "illumi".equals(str) ){
			return "�C���~�l�[�V����";
		}
		if( "backlight".equals(str) ){
			return "�o�b�N���C�g";
		}
		if( "pet".equals(str) ){
			return "�y�b�g";
		}
		if( "cooking".equals(str) ){
			return "����";
		}
		if( "off".equals(str) ){
			return "�I�t";
		}
		
	
		return str;
	}
	
	
}
