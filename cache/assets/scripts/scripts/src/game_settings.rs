pub use crate::prelude::*;

extern "Rust" {
    #[script_id(40)]
    pub fn _40(v0: int);

    #[script_id(85)]
    pub fn _85(v0: int, v1: int, v2: int);

    #[script_id(526)]
    pub fn _526(v0: int, v1: int, v2: int, v3: String, v4: int, v5: int);
}

#[script_id(10200)]
unsafe fn _10200(
    local_0: int,
    local_1: int,
    local_2: int,
    local_str_0: String,
    local_str_1: String,
) {
    let local_3 = local_0 * 6;
    let local_4 = interface!(1702, 7);
    let local_5 = interface!(1702, 9);
    cc_create(0, local_4, 3, local_3);
    cc_setsize(0, 173, 30, 0, 0);
    cc_setposition(0, 0, local_0 * 30, 0, 0);
    cc_setfill(0, true);
    cc_settrans(0, 166);
    if local_0 % 2 == 0 {
        cc_setcolour(0, 4536627);
    } else {
        cc_setcolour(0, 1509377);
    }
    cc_create(0, local_4, 4, local_3 + 1);
    cc_setsize(0, 120, 30, 0, 0);
    cc_setposition(0, 3, local_0 * 30, 0, 0);
    cc_settext(0, local_str_0);
    cc_settextshadow(0, true);
    cc_settextfont(0, 495);
    cc_setcolour(0, 16750623);
    cc_settextalign(0, 0, 1, 0);
    cc_setonmouserepeat(0, &|| {
        _526(-2147483645, -2147483643, local_5, local_str_1, 10, 250)
    });
    cc_setonmouseleave(0, &|| _40(local_5));
    cc_create(0, local_4, 5, local_3 + 2);
    cc_sethide(0, if local_1 != 0 { true } else { false });
    cc_setsize(0, 19, 19, 0, 0);
    cc_setposition(0, 132, local_0 * 30 + 5, 0, 0);
    if local_2 == 0 {
        cc_setgraphic(0, 698);
        cc_setop(0, 1, "<col=ff981f>Enable</col>");
    } else {
        cc_setgraphic(0, 699);
        cc_setop(0, 1, "<col=ff981f>Disable</col>");
    }
    cc_setopbase(0, local_str_0);
    cc_setonclick(0, &|| _10202(-2147483645, -2147483643));
    cc_create(0, local_4, 3, local_3 + 3);
    cc_sethide(0, if local_1 != 1 { true } else { false });
    cc_setsize(0, 21, 21, 0, 0);
    cc_setposition(0, 132, local_0 * 30 + 5, 0, 0);
    cc_setfill(0, true);
    cc_setcolour(0, 4340796);
    cc_create(0, local_4, 3, local_3 + 4);
    cc_sethide(0, if local_1 != 1 { true } else { false });
    cc_setsize(0, 22, 22, 0, 0);
    cc_setposition(0, 131, local_0 * 30 + 4, 0, 0);
    cc_setfill(0, false);
    cc_setcolour(0, 10066329);
    cc_create(0, local_4, 4, local_3 + 5);
    cc_sethide(0, if local_1 != 1 { true } else { false });
    cc_setsize(0, 21, 24, 0, 0);
    cc_setposition(0, 132, local_0 * 30 + 4, 0, 0);
    cc_settext(0, "");
    cc_settextshadow(0, true);
    cc_settextfont(0, 496);
    cc_setcolour(0, 11776947);
    cc_settextalign(0, 1, 1, 0);
    cc_setop(0, 1, "<col=ff981f>Modify</col>");
    cc_setopbase(0, local_str_0);
    cc_setonmouseover(0, &|| _85(-2147483645, -2147483643, 16777215));
    cc_setonmouseleave(0, &|| _85(-2147483645, -2147483643, 11776947));
    return;
}

#[script_id(10202)]
unsafe fn _10202(local_0: int, local_1: int) {
    if cc_find(0, local_0, local_1) {
        if compare(cc_getop(0, 1), "<col=ff981f>Enable</col>") == 0 {
            cc_setgraphic(0, 699);
            cc_setop(0, 1, "<col=ff981f>Disable</col>");
        } else {
            cc_setgraphic(0, 698);
            cc_setop(0, 1, "<col=ff981f>Enable</col>");
        }
        return;
    }
}
