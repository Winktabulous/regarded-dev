pub use super::types::*;
pub use scripts_macro::*;

pub const NULL_REF: &'static dyn FnOnce() = &|| {};

pub const white: int = 16777215;
pub const black: int = 0;
pub const yellow: int = 16776960;
pub const red: int = 16711680;
pub const green: int = 901389;
pub const orange: int = 16750899;
pub const blood_red: int = 0x880808;
pub const light_100_orange: int = 0xffcc99;
pub const light_300_orange: int = 0xffcc80;

pub const max_32bit_int: int = 2147483647;
pub const min_32bit_int: int = 0;

pub const iftype_model: int = 6;
pub const iftype_graphic: int = 5;
pub const iftype_rectangle: int = 3;
pub const iftype_text: int = 4;
pub const iftype_line: int = 9;

pub const setpos_abs_top: int = 0;
pub const setpos_abs_left: int = 0;
pub const setpos_abs_centre: int = 1;
pub const setpos_abs_right: int = 2;
pub const setpos_abs_bottom: int = 2;

pub const settextalign_left: int = 0;
pub const settextalign_right: int = 2;
pub const settextalign_centre: int = 1;
pub const settextalign_top: int = 0;
pub const settextalign_bottom: int = 2;

pub const setsize_minus: int = 1;
pub const setsize_abs: int = 0;

pub const p12_full: int = 495;
pub const synth_2266: int = 0;
pub const pillory_wrong: int = 2277;
pub const event_mousex: int = -2147483647;
pub const event_mousey: int = -2147483646;
pub const event_com: int = -2147483645;
pub const event_comid: int = -2147483643;
pub const event_comsubid: int = -2147483643;
pub const event_opdot_widget: int = -2147483644;
pub const event_dragtarget: int = -2147483642;
pub const event_dropsubid: int = -2147483641;
pub const event_dragtargetid: int = -2147483641;
pub const event_keypressed: int = -2147483640;
pub const event_keytyped: int = -2147483639;

pub const key_f1: int = 1;
pub const key_f2: int = 2;
pub const key_f3: int = 3;
pub const key_f4: int = 4;
pub const key_f5: int = 5;
pub const key_f6: int = 6;
pub const key_f7: int = 7;
pub const key_f8: int = 8;
pub const key_f9: int = 9;
pub const key_f10: int = 10;
pub const key_f11: int = 11;
pub const key_f12: int = 12;

pub const p11_full: int = 494;
pub const b12_full: int = 496;

pub const inv: int = 93;
pub const bank: int = 95;
pub const worn: int = 94;

pub const key_return: int = 84;
pub const key_page_up: int = 104;
pub const key_page_down: int = 105;
pub const key_tab: int = 80;

pub const enum_obj_type: int = 111;
pub const enum_enum_type: int = 103;
pub const enum_int_type: int = 105;
pub const enum_component_type: int = 73;
pub const enum_struct_type: int = 74;
pub const enum_stat_type: int = 83;
pub const enum_boolean_type: int = 49;
pub const enum_graphic_type: int = 100;
pub const enum_namedobj_type: int = 79;
pub const enum_string_type: int = 115;

pub const scrollbar_dragger_v2_3: int = 792;
pub const scrollbar_dragger_v2_0: int = 789;
pub const scrollbar_dragger_v2_1: int = 790;
pub const scrollbar_dragger_v2_2: int = 791;
pub const scrollbar_v2_0: int = 773;
pub const scrollbar_v2_1: int = 788;

pub const enum_int: int = 105;
pub const enum_string: int = 115;

pub const windowmode_fixed: int = 1;

pub const null: int = -1;

pub const tradebacking_light: int = 1040;

pub const combatboxesmed_0: int = 812;

pub const v2_stone_close_button_0: int = 831;
pub const v2_stone_close_button_1: int = 832;

extern "Rust" {
    /// Clears all of the elements of array at index `idx` in the runescape
    /// array store, then sets the value to `-1` and sets the size of newly
    /// initialized array to `size`.
    ///
    /// `idx`   - the index that references specific array.
    /// `size`  - the size of newly initialized array. Runescape only supports
    ///           five arrays so index must be in range of 0 to 4.
    pub fn new_array(idx: int, size: int) -> Array;

    /// Gets the previously initialized array reference with the specific index.
    ///
    /// `idx`   - the index that references specific array. Runescape only supports
    ///           five arrays so index must be in range of 0 to 4.
    pub fn get_array(idx: int) -> Array;

    #[opcode(4018)]
    pub fn scale(v0: int, v1: int, v2: int) -> int;

    #[opcode(1)]
    #[first_arg_is_operand]
    pub fn get_varp(v0: int) -> int;

    #[opcode(2)]
    #[first_arg_is_operand]
    pub fn set_varp(v0: int, v1: int) -> int;

    #[opcode(25)]
    #[first_arg_is_operand]
    pub fn get_varp_bit(v0: int) -> int;

    #[opcode(27)]
    #[first_arg_is_operand]
    pub fn set_varp_bit(v0: int, v1: int) -> int;

    #[opcode(42)]
    #[first_arg_is_operand]
    pub fn get_varc_int(v0: int) -> int;

    #[opcode(43)]
    #[first_arg_is_operand]
    pub fn set_varc_int(v0: int, v1: int) -> int;

    #[opcode(49)]
    #[first_arg_is_operand]
    pub fn get_varc_string(v0: int) -> String;

    #[opcode(50)]
    #[first_arg_is_operand]
    pub fn set_varc_string(v0: int, string: String);

    #[opcode(74)]
    #[first_arg_is_operand]
    pub fn get_var_clan_setting(v0: int) -> int;

    #[opcode(76)]
    #[first_arg_is_operand]
    pub fn get_var_clan(v0: int) -> int;

    #[opcode(3200)]
    pub fn sound_synth(v0: int, v1: int, v2: int);

    #[opcode(3612)]
    pub fn clan_getchatcount() -> int;

    #[opcode(3620)]
    pub fn clan_leavechat();

    #[opcode(3100)]
    pub fn mes(v0: String);

    #[opcode(4101)]
    pub fn append(v0: String, v1: String) -> String;

    #[opcode(4103)]
    pub fn lowercase(v0: String) -> String;

    #[opcode(4105)]
    pub fn text_gender(v0: String, v1: String) -> String;

    #[opcode(4108)]
    pub fn paraheight(v0: String, v1: int, font: int) -> int;

    #[opcode(4111)]
    pub fn escape(v0: String) -> String;

    #[opcode(4106)]
    pub fn to_string(v0: int) -> String;

    #[opcode(4112)]
    pub fn append_char(v0: String, v1: int) -> String;

    #[opcode(4113)]
    pub fn char_isprintable(v0: int) -> String;

    #[opcode(4117)]
    pub fn string_length(v0: String) -> int;

    #[opcode(4118)]
    pub fn substring(v0: String, v1: int, v2: int) -> String;

    #[opcode(4119)]
    pub fn removetags(v0: String) -> String;

    #[opcode(4200)]
    pub fn oc_name(v0: int) -> String;

    #[opcode(4202)]
    pub fn oc_iop(v0: int, v1: int) -> String;

    #[opcode(4203)]
    pub fn oc_cost(v0: int) -> int;

    #[opcode(4204)]
    pub fn oc_stackable(v0: int) -> bool;

    #[opcode(4206)]
    pub fn oc_uncert(v0: int) -> int;

    #[opcode(4207)]
    pub fn oc_members(v0: int) -> bool;

    #[opcode(4209)]
    pub fn oc_unplaceholder(v0: int) -> int;

    #[opcode(3312)]
    pub fn map_members() -> bool;

    #[opcode(4107)]
    pub fn compare(v0: String, v1: String) -> int;

    #[opcode(3408)]
    pub fn get_enum(key_type: int, value_type: int, id: int, dot_widget: int) -> int;

    #[opcode(3408)]
    pub fn get_enum_as_boolean(key_type: int, value_type: int, id: int, dot_widget: int) -> bool;

    #[opcode(3408)]
    pub fn get_enum_as_string(key_type: int, value_type: int, id: int, dot_widget: int) -> String;

    #[opcode(3400)]
    pub fn get_enum_string(key_type: int, value_type: int, id: int, dot_widget: int) -> String;

    #[opcode(4109)]
    pub fn parawidth(v0: String, v1: int, v2: int) -> int;

    #[opcode(3411)]
    pub fn enum_getoutputcount(v0: int) -> int;

    // interface instructions
    #[opcode(201)]
    pub fn if_find(component: Component) -> bool;

    #[opcode(1700)]
    pub fn cc_getinvobject(v0: int) -> int;

    #[opcode(1609)]
    #[first_arg_is_operand]
    pub fn cc_gettrans(dot_widget: int) -> int;

    #[opcode(1801)]
    #[first_arg_is_operand]
    pub fn cc_getop(dot_widget: int, index2: int) -> String;

    #[opcode(1802)]
    #[first_arg_is_operand]
    pub fn cc_getopbase(dot_widget: int) -> String;

    #[opcode(2000)]
    pub fn if_setposition(v0: int, v1: int, v2: int, v3: int, component: Component);

    #[opcode(2003)]
    pub fn if_sethide(v0: bool, component: Component);

    #[opcode(2005)]
    pub fn if_setnoclickthrough(v0: bool, component: Component);

    #[opcode(2001)]
    pub fn if_setsize(v0: int, v1: int, v2: int, v3: int, component: Component);

    #[opcode(2100)]
    pub fn if_setscrollpos(v0: int, v1: int, component: Component);

    #[opcode(2101)]
    pub fn if_setcolour(v0: int, component: Component);

    #[opcode(2200)]
    pub fn if_setobject(v0: int, v1: int, component: Component);

    #[opcode(2103)]
    pub fn if_settrans(dot_widget: int, component: Component);

    #[opcode(2105)]
    pub fn if_setgraphic(graphic: int, component0: Component);

    #[opcode(2112)]
    pub fn if_settext(text: String, v1: Component);

    #[opcode(2113)]
    pub fn if_settextfont(v0: int, v1: Component);

    #[opcode(2114)]
    pub fn if_settextalign(v0: int, v1: int, v2: int, component: Component);

    #[opcode(2115)]
    pub fn if_settextshadow(shadow: bool, v1: Component);

    #[opcode(2116)]
    pub fn if_setoutline(v1: int, v1: Component);

    #[opcode(2118)]
    pub fn if_setvflip(v0: bool, dot_widget: int);

    #[opcode(2119)]
    pub fn if_sethflip(v0: bool, dot_widget: int);

    #[opcode(2120)]
    pub fn if_setscrollsize(v0: int, v1: int, component: Component);

    #[opcode(2801)]
    pub fn if_getop(v0: int, v1: int) -> String;

    #[opcode(2802)]
    pub fn if_getop_base(v0: int) -> String;

    #[opcode(2502)]
    pub fn if_getwidth(v0: int) -> int;

    #[opcode(2503)]
    pub fn if_getheight(component: Component) -> int;

    #[opcode(2300)]
    pub fn if_setop(v0: int, text: String, v1: int);
    #[opcode(2307)]
    pub fn if_clearops(component: Component);

    // interface hooks
    #[opcode(2407)]
    pub fn if_setonvartransmit(component: Component, script: ScriptReference, varps: &[int]);

    #[opcode(2408)]
    pub fn if_setontimer(component: Component, script: ScriptReference);

    #[opcode(2414)]
    pub fn if_setoninvtransmit(component: Component, script: ScriptReference, varps: &[int]);

    #[opcode(2412)]
    pub fn if_setonmouserepeat(component: Component, script: ScriptReference);

    #[opcode(2415)]
    pub fn if_setonstattransmit(component: Component, script: ScriptReference, stats: &[int]);

    #[opcode(2417)]
    pub fn if_setonscrollwheel(component: Component, script: ScriptReference);

    #[opcode(2404)]
    pub fn if_setonmouseleave(component: Component, script: ScriptReference);

    #[opcode(2409)]
    pub fn if_setonop(component: Component, script: ScriptReference);

    #[opcode(2400)]
    pub fn if_setonclick(component: Component, script: ScriptReference);

    #[opcode(2419)]
    pub fn if_setonkey(component: Component, script: ScriptReference);

    #[opcode(2403)]
    pub fn if_setonmouseover(component: Component, script: ScriptReference);

    #[opcode(2423)]
    pub fn if_seton_dialog_abort(component: Component, script: ScriptReference);

    #[opcode(2424)]
    pub fn if_setonsubchange(component: Component, script: ScriptReference);

    #[opcode(2427)]
    pub fn if_setonresize(component: Component, script: ScriptReference);

    #[opcode(2500)]
    pub fn if_getx(component: Component) -> int;

    #[opcode(2501)]
    pub fn if_gety(component: Component) -> int;

    #[opcode(2504)]
    pub fn if_gethide(component: Component) -> bool;

    #[opcode(2505)]
    pub fn if_getlayer(component: Component) -> int;

    #[opcode(2600)]
    pub fn if_getscrollx(component: Component) -> int;

    #[opcode(2601)]
    pub fn if_getscrolly(component: Component) -> int;

    #[opcode(2604)]
    pub fn if_getscrollheight(component: Component) -> int;

    #[opcode(2706)]
    pub fn if_gettop() -> int;

    #[opcode(3308)]
    pub fn coord() -> int;

    #[opcode(3309)]
    pub fn coordx(coord: int) -> int;

    #[opcode(3310)]
    pub fn coordz(coord: int) -> int;

    #[opcode(3311)]
    pub fn coordy(coord: int) -> int;

    #[opcode(3324)]
    pub fn worldflags() -> int;

    // child component instructions
    #[opcode(200)]
    #[first_arg_is_operand]
    pub fn cc_find(is_dot_widget: int, component: Component, component2: Component) -> bool;

    #[opcode(100)]
    #[first_arg_is_operand]
    pub fn cc_create(is_dot_widget: int, component: Component, v0: int, v1: int);

    #[opcode(102)]
    #[first_arg_is_operand]
    pub fn cc_deleteall(dot_widget: int, component: Component);

    #[opcode(4006)]
    pub fn interpolate(v0: int, v1: int, v2: int, v3: int, v4: int) -> int;

    #[opcode(4008)]
    pub fn setbit(v0: int, v1: int) -> int;

    #[opcode(4010)]
    pub fn testbit(v0: int, v1: int) -> bool;

    #[opcode(4012)]
    pub fn pow(v0: int, v1: int) -> int;

    #[opcode(4121)]
    pub fn string_indexof_string(v0: String, v1: String, v2: int) -> int;

    #[opcode(6205)]
    pub fn viewport_getfov() -> (int, int);

    #[opcode(6512)]
    pub fn setfolloweropslowpriority(v0: bool);

    /// Sets the position of child component
    ///
    /// `index` - the index of a child component.
    /// `x`     - the x location.
    /// `y`     - the y location.
    /// `horizontal_placement` - the horizontal alignment type.
    /// `vertical_placement`   - the vertical alignment type.
    #[opcode(1000)]
    #[first_arg_is_operand]
    pub fn cc_setposition(
        dot_widget: int,
        x: int,
        y: int,
        horizontal_alignment: int,
        vertical_alignment: int,
    );

    #[opcode(1001)]
    #[first_arg_is_operand]
    pub fn cc_setsize(dot_widget: int, v0: int, v1: int, v2: int, v3: int);

    #[opcode(1003)]
    #[first_arg_is_operand]
    pub fn cc_sethide(dot_widget: int, v0: bool);

    #[opcode(1005)]
    #[first_arg_is_operand]
    pub fn cc_setnoclickthrough(dot_widget: int, v0: bool);

    #[opcode(1103)]
    #[first_arg_is_operand]
    pub fn cc_settrans(dot_widget: int, v0: int);

    #[opcode(1105)]
    #[first_arg_is_operand]
    pub fn cc_setgraphic(dot_widget: int, v0: int);

    #[opcode(1101)]
    #[first_arg_is_operand]
    pub fn cc_setcolour(dot_widget: int, v0: int);

    #[opcode(1102)]
    #[first_arg_is_operand]
    pub fn cc_setfill(dot_widget: int, v0: bool);

    #[opcode(1108)]
    #[first_arg_is_operand]
    pub fn cc_setmodel(dot_widget: int, v0: int);

    #[opcode(1109)]
    #[first_arg_is_operand]
    pub fn cc_setmodelangle(dot_widget: int, v0: int, v1: int, v2: int, v3: int, v4: int, v5: int);

    #[opcode(1110)]
    #[first_arg_is_operand]
    pub fn cc_setmodelanim(dot_widget: int, v0: int);

    #[opcode(1115)]
    #[first_arg_is_operand]
    pub fn cc_settextshadow(dot_widget: int, v0: bool);

    #[opcode(1114)]
    #[first_arg_is_operand]
    pub fn cc_settextalign(dot_widget: int, v0: int, v1: int, v2: int);

    #[opcode(1116)]
    #[first_arg_is_operand]
    pub fn cc_setoutline(dot_widget: int, v0: int);

    #[opcode(1117)]
    #[first_arg_is_operand]
    pub fn cc_setgraphicshadow(dot_widget: int, v0: int);

    #[opcode(1118)]
    #[first_arg_is_operand]
    pub fn cc_setvflip(dot_widget: int, v0: bool);

    #[opcode(1119)]
    #[first_arg_is_operand]
    pub fn cc_sethflip(dot_widget: int, v0: bool);

    #[opcode(1112)]
    #[first_arg_is_operand]
    pub fn cc_settext(dot_widget: int, v0: String);

    #[opcode(1200)]
    #[first_arg_is_operand]
    pub fn cc_setobject(dot_widget: int, v0: int, v1: int);

    #[opcode(1205)]
    #[first_arg_is_operand]
    pub fn cc_setobject_nonum(dot_widget: int, v0: int, v1: int);

    #[opcode(1300)]
    #[first_arg_is_operand]
    pub fn cc_setop(dot_widget: int, v0: int, v1: String);

    #[opcode(1301)]
    #[first_arg_is_operand]
    pub fn cc_setdraggable(dot_widget: int, v0: Component, v1: int);

    #[opcode(1302)]
    #[first_arg_is_operand]
    pub fn cc_setdragdeadzone(dot_widget: int, v0: int);

    #[opcode(1303)]
    #[first_arg_is_operand]
    pub fn cc_setdraggablebehavior(dot_widget: int, v0: int);

    #[opcode(1304)]
    #[first_arg_is_operand]
    pub fn cc_setdragdeadtime(dot_widget: int, v0: int);

    #[opcode(1305)]
    #[first_arg_is_operand]
    pub fn cc_setopbase(dot_widget: int, v0: String);

    #[opcode(1307)]
    #[first_arg_is_operand]
    pub fn cc_clearops(dot_widget: int);

    #[opcode(1350)]
    #[first_arg_is_operand]
    pub fn cc_setopkey(
        dot_widget: int,
        v0: int,
        v1: int,
        v2: int,
        v3: int,
        v4: int,
        v5: int,
        v6: int,
        v7: int,
        v8: int,
        v9: int,
    );

    #[opcode(1352)]
    #[first_arg_is_operand]
    pub fn cc_setopkeyrate(dot_widget: int, v0: int, v1: int);

    #[opcode(1400)]
    #[first_arg_is_operand]
    pub fn cc_setonclick(dot_widget: int, script: ScriptReference);

    #[opcode(1401)]
    #[first_arg_is_operand]
    pub fn cc_setonhold(dot_widget: int, script: ScriptReference);

    #[opcode(1403)]
    #[first_arg_is_operand]
    pub fn cc_setonmouseover(dot_widget: int, script: ScriptReference);

    #[opcode(1404)]
    #[first_arg_is_operand]
    pub fn cc_setonmouseleave(dot_widget: int, script: ScriptReference);

    #[opcode(1405)]
    #[first_arg_is_operand]
    pub fn cc_setondrag(dot_widget: int, script: ScriptReference);

    #[opcode(1408)]
    #[first_arg_is_operand]
    pub fn cc_setontimer(dot_widget: int, script: ScriptReference);

    #[opcode(1409)]
    #[first_arg_is_operand]
    pub fn cc_setonop(dot_widget: int, script: ScriptReference);

    #[opcode(1410)]
    #[first_arg_is_operand]
    pub fn cc_setondragcomplete(dot_widget: int, script: ScriptReference);

    #[opcode(1412)]
    #[first_arg_is_operand]
    pub fn cc_setonmouserepeat(dot_widget: int, script: ScriptReference);

    #[opcode(1414)]
    #[first_arg_is_operand]
    pub fn cc_setoninvtransmit(dot_widget: int, script: ScriptReference, invs: &[int]);

    #[opcode(1417)]
    #[first_arg_is_operand]
    pub fn cc_setonscrollwheel(dot_widget: int, script: ScriptReference);

    #[opcode(1419)]
    #[first_arg_is_operand]
    pub fn cc_setonkey(dot_widget: int, script: ScriptReference);

    #[opcode(1407)]
    #[first_arg_is_operand]
    pub fn cc_setonvartransmit(dot_widget: int, script: ScriptReference, varps: &[int]);

    #[opcode(1500)]
    #[first_arg_is_operand]
    pub fn cc_getx(dot_widget: int) -> int;

    #[opcode(1501)]
    #[first_arg_is_operand]
    pub fn cc_gety(dot_widget: int) -> int;

    #[opcode(1502)]
    #[first_arg_is_operand]
    pub fn cc_getwidth(dot_widget: int) -> int;

    #[opcode(1503)]
    #[first_arg_is_operand]
    pub fn cc_getheight(dot_widget: int) -> int;

    #[opcode(1611)]
    #[first_arg_is_operand]
    pub fn cc_getcolor(dot_widget: int) -> int;

    #[opcode(1702)]
    #[first_arg_is_operand]
    pub fn cc_id(dot_widget: int) -> int;

    #[opcode(1113)]
    #[first_arg_is_operand]
    pub fn cc_settextfont(dot_widget: int, v0: int);

    #[opcode(1107)]
    #[first_arg_is_operand]
    pub fn cc_settiling(dot_widget: int, v0: bool);

    #[opcode(2305)]
    pub fn if_setopbase(v0: String, v1: Component);

    #[opcode(3300)]
    pub fn clientclock() -> int;

    #[opcode(3110)]
    pub fn mousecam(v0: bool);

    #[opcode(3111)]
    pub fn getremoveroofs() -> bool;

    #[opcode(3112)]
    pub fn setremoveroofs(v0: bool);

    #[opcode(3301)]
    pub fn inv_getobj(v0: int, v1: int) -> int;

    #[opcode(3302)]
    pub fn inv_getnum(v0: int, v1: int) -> int;

    #[opcode(3303)]
    pub fn inv_total(inv: int, item: int) -> int;

    #[opcode(3304)]
    pub fn inv_size(v0: int) -> int;

    #[opcode(3609)]
    pub fn friend_test(v0: String) -> bool;

    #[opcode(3316)]
    pub fn staffmodlevel() -> int;

    #[opcode(4120)]
    pub fn string_indexof_char(v0: String, v1: int) -> int;

    #[opcode(5008)]
    pub fn chat_sendpublic(v0: String, v1: int);

    #[opcode(5015)]
    pub fn chat_playername() -> String;

    #[opcode(5306)]
    pub fn getwindowmode() -> int;

    #[opcode(6515)]
    pub fn oc_param(v0: int, v1: int) -> int;

    #[opcode(6515)]
    pub fn oc_param_str(v0: int, v1: int) -> String;

    #[opcode(6516)]
    pub fn struct_param(v0: int, v1: int) -> int;

    #[opcode(6516)]
    pub fn struct_param_str(v0: int, v1: int) -> String;

    #[opcode(6518)]
    pub fn on_mobile() -> bool;

    #[opcode(6519)]
    pub fn client_type() -> int;

    #[opcode(3103)]
    pub fn if_close();

    #[opcode(3104)]
    pub fn resume_count_dialog(v0: int);

    #[opcode(3105)]
    pub fn resume_named_dialog(v0: String);

    #[opcode(3106)]
    pub fn resume_string_dialog(v0: String);

    #[opcode(3317)]
    pub fn reboot_timer() -> int;

    #[opcode(3616)]
    pub fn clan_get_chat_min_kick() -> int;

    #[opcode(3618)]
    pub fn clan_get_chat_rank() -> int;

    #[opcode(5003)]
    pub fn chat_gethistory_by_type_and_line(
        v0: int,
        v1: int,
    ) -> (int, int, String, String, String, int);

    #[opcode(5004)]
    pub fn chat_gethistory_by_uid(v1: int) -> (int, int, String, String, String, int);

    #[opcode(5017)]
    pub fn chat_gethistorylength(v0: int) -> int;

    #[opcode(5019)]
    pub fn chat_get_prev_uid(v0: int) -> int;

    #[opcode(5022)]
    pub fn chat_getmessagefilter() -> String;

    #[opcode(5030)]
    pub fn chat_gethistory_by_type_and_line_v2(
        v0: int,
        v1: int,
    ) -> (int, int, String, String, String, int, String, int);

    #[opcode(5031)]
    pub fn _op_5031(
        v0: int,
    ) -> (int, int, String, String, String, int, String, int);

    #[opcode(3800)]
    pub fn active_clan_settings_find_listened() -> bool;

    #[opcode(3801)]
    pub fn active_clan_settings_find_affined(v0: int) -> bool;

    #[opcode(3802)]
    pub fn active_clan_settings_get_clan_name() -> String;

    #[opcode(3803)]
    pub fn active_clan_settings_get_allow_unaffined() -> bool;

    #[opcode(3804)]
    pub fn active_clan_settings_get_rank_talk() -> int;

    #[opcode(3805)]
    pub fn active_clan_settings_get_rank_kick() -> int;

    #[opcode(3850)]
    pub fn active_clan_channel_find_listened() -> bool;

    #[opcode(3851)]
    pub fn active_clan_channel_find_affined(v0: int) -> bool;

    #[opcode(3852)]
    pub fn active_clan_channel_get_clan_name() -> String;

    #[opcode(3853)]
    pub fn active_clan_channel_get_rank_kick() -> int;

    #[opcode(3857)]
    pub fn active_clan_channel_get_user_rank(v0: int) -> int;

    #[opcode(3860)]
    pub fn active_clan_channel_get_user_slot(v0: String) -> int;

    #[opcode(4123)]
    pub fn _op_4123(v0: String) -> int;

    #[opcode(4029)]
    pub fn get_bit_range(v0: int, v1: int, v2: int) -> int;

}
