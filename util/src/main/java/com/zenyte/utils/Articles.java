package com.zenyte.utils;

/**
 * Used for determining which indefinite article ("a" or "an") an English word should be preceded by.
 */
public final class Articles {
    // enwiki-20181020-pages-articles
    private static final Node NODE = new NodeParser().parse("wdf4p;6ci2r;2m;#34c;2n;1;ig;1a;;&5e;1h;1;N;5;;*136;7f;4;a2;x;;e1;u;;i2;n;;o;7;;.fti;g8;1;86a;4w;1;8;;1;01;9;;/ty;75;a;a3;o;;e4;k;;hb;n;1;o6;;;i;p;;l;;1;/;8;;m;;1;/;5;;n;;1;o7;;;o1;d;;r;;1;e8;;;s;;1;/4;p;;0fka;1bs;1;–7qk;gp;1;11kz;40;1;82;c;;1asrk;ypd;2;14cx;a6u;a;0wj;3w;1;010;2y;;1bq;15;;2fh;y;1;0;5;;3a6;17;;4ba;r;;5fo;y;;6a3;v;;78i;11;;89r;13;1;6;6;;97x;x;;82yh;nv3;c;01bf;j0;a;0p;5b;;12;1q;;22;1b;;32;1g;;4;1f;;53;1l;;61;1e;;71;1a;;82;14;;91;22;;127;fp;2; z;2;;.5;;;233;l5;3; 1q;;;.8;;;–9;;;32r;q2;1; 1c;1;;43z;sp;3; 1t;1;;,7;;;–5;;;57j;145;5; 3d;1;;,o;;;h8;;;kp;;;m6;;;64e;13c;1; 1u;3;;74b;1b9;3; 1n;;;,5;;;–9;;;84r;1r2;2; 1n;2;;,7;;;95v;22y;2; 1d;1;;–6;;;X5;;;pf;3;;82ns;uq1;2;566;1fz;1;0y;57;1;M;;1;H;5;;945;sf;1;85;;;<g2;2b;3;a1n;h;1;>;5;;m;;1;a5;;;o;8;;=j9;2j;1;=40;i;1;E;7;;@1i;2s;1;#5;;;A54l;g1uq;6;Aeg;1ac;1;Aa7;89;1;Sa;y;;i3o;5pv;1;r36;596;1;ob;2;;m9x;75je;1;a21;1jq;1;rw;1n;1; t;1;;ncl;lu8;1;d4v;20x;1;a3c;7f;1;l3b;65;1;u34;5i;1;c30;k;;rfn;1534;2;a2a;6ub;2;g8;4l;1;ó5;;;kb;w;1;k9;1;;m23;8gc;1;ap;54;1;tj;4;;s5q;kkt;2;aj;3l;1;ni;12;1;sh;2;;t1e;ym;1;u10;2i;1;r10;2g;1;i10;2e;1;a10;2d;1;sw;1;;B9pk9;r0;2;hv0;6c;2;ago;49;1;if;28;1;rc;20;1;ab;;;á;10;;or9f;2f;1;r1mm;s;1;di0;k;1; 7;k;;Cb748;uv;2;h2d3p;a9;5;acuc;20;2;i1d8;r;1;s;;1;e6;;;o5q;7;1;i;6;;l1c;r;1;á;h;;o1av;1u;4;i5v;l;1;r1i;e;1;c;7;;mz;c;1;h;b;;n1i;d;1;gr;a;1;r;;1;o;5;;r8a;b;1;r;5;;rpoh;1t;1;ocz;b;1;s;5;;u3s7;w;1;i2;f;;o2f3a;6h;3;i33;d;1;m;;1;i;8;;np65;1g;1;g487;b;1;j;5;;s1lw;b;1;á;5;;D569f;gw;6;aov7;1w;1;iw1;e;1;m60;7;1;h;7;;ojii;24;2;i;;1;r;8;;m2xr;g;1;h3;c;;up48;21;2;adw;f;1;ld5;f;1;c;f;;b11r;c;1;h;6;;á49;c;1; ;9;;ù1;f;;ư;6;;Elge;5a2f;9;F1v;1mp;1;P9;o;1;S9;;;U5a;1ie;1;R2e;7;;bg;79;1;od;4o;1;lb;3f;1;ib;;;m9v;91n;1;pz;1el;1;eo;a8;1;zk;;;n9p;2vhp;1;ag;1j;1;me;3;;r28;1p7;1;or;1i;1;dp;7;;s1rt;46t;2;p1p5;aj;3;e7;84;;h;m;;r;h;;t1d;2m5;1;ak;51;1;r5;;;uhmu;gz;1;lm;8a;1;o8;1;;x6y;865;1;ck;vp;1;ef;m4;1;la;in;1;ê5;;;F5ubo;mjj;11; er;294;;,1;1b;;.33;6b;;/t;31;;05;6a;;134;jh;;215;d5;;319;9x;;416;ap;;5p;7k;;69;14;;7;;1;09;;;86;w;;96;l;;A4lr;6j3;e;Cyb;qt;;D15;h;;Fc;;;I26;3r;2;Lb;;;Rl;;;K1d;1;;Ls;4;;M2f;2;;N1c;5;;P1t;3;;Qb8;70;;Rbn;45;;S1z;e;;T30;3;;X8;1;;B76;25k;;C6m;p8;3;,b;;;Mb;2;;O;;1; ;6;;D3k;du;;E9l;3j;2;Ca;n;;I6;k;;F3u;88;;H1k;25;1;Dd;1;;I2is;ga;3;A34;5i;1;Tq;3;;R48;5t;3;A6;;;Ed;;;S2f;;;U2;d;;Lou;j9;2;C3c;4r;;P1;e;;M7h;1wm;;Okf;35;1; 5;h;;Puy;118;1;.7x;57;;Ral;4s;2;A1j;i;1; 4;f;;Se;10;;S4o;a7;;T9m;276;1;S2k;7;;Ugy;45;1;.;c;;Xu;2f;;Y1e;88;2; 5;;;V6;;;c2;v;;f;;1;o7;;;h1;3n;;x;5;;σ1;f;;G6phz;fo;6;e2cz6;1z;1;r1x5l;p;1;n;;1;e;a;;h2zg;2s;1;a2a1;21;3;e;o;;i;l;;o;6;;o12hp;1w;2;fq;a;1; ;9;;t2xf;e;1;t35;6;1; ;5;;r1lfl;26;1;itz;b;1;a;7;;w4l;8;1;a1z;7;1;r;;1;r;6;;ú;5;;H2p96;gqk;s; 7t;u7;;&k;1r;;+d;s;;,;n;;.;;4;Aj;;;B5;;;Is;4;;O9;1;;1l;1v;;217;2f;;3d;w;;5a;1a;;72;a;;B49;gr;1;P9;;;C2f;3t;;Dga;zw;1;Pk;1;;F2a;3j;1;/n;7;;Hy;4a;;Ib1;on;9;Af;4;;Cb;2;;F8;1;;G1c;3;;Ml;2;;Nb;1;;P13;1;;S;;1;D1;q;;Tm;1;;K5w;2d;1;9;7;;L1y;3o;;M3u;7w;2;.b;1;;M;;1; 5;i;;Nl;3w;;Oam;4m;3; e;z;;A1s;n;1; 5;g;;Vd;1n;;P50;cy;;R3p;6f;1;To;8;;Tbp;17x;;V1m;42;;b5;p;;ediy;8s;2;iru;1k;1;r4;17;;r307;2k;1;r1x;b;1;n;7;;omjh;8u2;3;n50q;8kl;6;d1ej;e;;e5q;1z;1;s5;1z;;g319;v;;k2d;;;obx;8fe;1;l2l;1;;v5;;;t1cm;z;1;e6e;g;1;l6d;g;1;i;5;;u3i1;49;1;r3;41;;I2v6;6ek2;3;I5f;bj;2;*6;;;I22;r;;nn8;25vp;1;d9y;1fp5;1;e1m;59b;1;p1a;51y;1;e1a;51x;1;n1a;51v;1;d1a;51v;1;êb;;;t37;174p;1;a27;16t9;1;rt;6;;J2r7h;75;1;i14b;w;1;a1o;q;1;o6;n;;K1jjd;6p;1;w5t;9;1;ä;7;;L3n3p;czb;11; 8n;1rz;;,4;10;;.2w;b5;;/b;t;;111;2b;;2s;3b;;3f;16;;4b;s;;74;e;;81;8;;Ar4;gi;5; 1w;9c;;A2;f;;B;;1; 4;f;;P14;3y;;X2;a;;B1w;3n;;C62;w2;;D4e;pu;;E;;3; 3;l;;D6b;ik;1;E6;;;I1;9;;F2a;46;1;Tf;5;;Gb2;zt;;H;;1;Dp;8;;Igw;1z;1;Rk;y;1;G6;;;Lgj;25f;;M5d;bj;;N3g;7e;;P7y;1qk;;R2h;8s;;S3v;i8;1;m7;;;T4y;e3;;U2f;r;1; 7;j;;V1j;4m;;X9;o;;Ze;10;;^;6;;au5y;9z;2;e;;1;s8;;;ocg;m;1;i;;1;sj;1;;lad;6;1;.;5;;oqxk;4c;1;c2xu;1m;1;h;;1;a8;15;1;b7;;;p1;b;;u6vs;20;1;s6b;13;1;hg;13;1;ob;;;é2x;9;1;i;8;;M6vtw;124c;16; 85;rc;;&d;1z;;,;j;;.200;6ti;3;Aq4;38n;1;.om;35f;1;D5;;;Cd;w;1;h6;;;P3a;bt;1;.1i;6c;1;S5;;;/6;1i;;15b;j4;;22w;86;;31i;3a;;420;75;;519;1u;1;.i;4;;619;4c;;7z;1s;1;.h;2;;8a;16;;A1ri;3x8;h;Ccj;2;;D2i;3;;F14;3;;G1n;5;;J3z;;;K6;;;L1i;4;;Mz;1;;N3q;d;;P2x;4;;R3a;1;;S3r;8;;T33;k;;Vo;2;;X2c;;;Yb;;;Zb;;;Bfx;4oe;2;B23;32;1;/5;;;Sg;11;1;Tc;1;;C77;t4;;D62;x0;;Eml;gh;4;G;;1; ;6;;P1w;bt;1;S5;;;W;;1;A5;;;n2;p;;Fa3;1oy;;G46;cb;;H3n;8v;1;Pe;4;;I16b;oy;6; f;19;;,1;8;;5b;3k;;6j;47;;L6i;m;1;B;d;;T2n;ak;1;Mo;1;;K25;3v;1;I9;1;;Li3;1yp;;Mdj;y2;;N1s;4p;;O11e;do;2;T;;2;O5;;;U5;;;U1g;3c;;Pjg;53f;;Ql;1t;;R65;x6;;Ss2;29q;;Tap;196;1;K5;;;Ubq;22;2; 4;l;;V2;d;;V41;e8;;Xu;2z;;Z;;1;R5;;;a2h8a;87;1;c55b;o;1;a73;6;1;o;;1;i;6;;b;;1;u;;1;m1;c;;f3e;is;;hf;1t;;osnx;am;2;De;u;1;e8;;;U38;5f;;p;;1;31;9;;N4vrt;oby;y; 9q;z9;;+;a;;.;;1;D;5;;46;l;;7;7;;8;9;;=;;1;1;5;;A5i1;re;5; o;19;;A4v;9i;2;C3o;94;1; c;2;;Fo;3;;Ca2;33;1;T1;g;;I2n;6s;1;R6;;;S37b;2q;1;Le;1p;;B95;3e8;;Ctl;3gx;1;Cu;1f;1;A;;1; ;5;;D4p;e8;;Eoz;c0;3;A1b;3l;1;F5;;;Hc;1k;;S;;1; 1p;32;;Fdg;3hx;;G7o;1e8;;Hb5;23b;;I;;9;C2a;s;;D;;1;A8;1;;I5;;;K7;;;Ld;2;;Np;1;;Oc;1;;P9;1;;S36;s;;K1l;5k;;L43;bi;;M2o;d4;;Nhe;3d;2;O;5;;R2;d;;Oy6;8e;1;C34;5q;;P;;2;O2ih;1sm;2;L1;s;;V2hn;1qp;1;/k;a;1;B;5;;P1j;y;1;O;6;;Rav;kx;1;T3x;d;;Si8;tj;2;F28;6f;1;We;3;;W8d;y;;T4r;bc;1; 1h;p;;V1k;4d;;W87;6o;3;A1g;3r;;O;;1;H;8;;Sl;14;;X1q;7i;;Y11n;ec;2;P1y;5v;;Uq;20;;Z8y;2o;1;I4;h;;a1as1;4q;1;sxv;i;1;a;;1; 5;;;t;;1;i6;;;vy;27;;²;6;;O28p;1ubm;7;N1l;4j;1;E1f;3;;ao;my;1;xe;3;;l31;bh8;1;vr;1;;n12t;1zh;2;ce;r;1;ee;1;;e10v;2q;1;i2;1z;;oq;3u;1;ph;6;;r54;a78;1;d12;1r4;1;em;15z;1;m7;;;u4w;1ug;1;i3x;k;;P71hs;it;1;hvne;3u;2;i4ol;13;1;a;h;;o18z;1q;2;bj;17;2;i7;;;oc;;;i2;j;;Qfno;1s;1;icz;q;1;n9x;i;1;g8c;i;1;x1;e;;R50f1;11pm;12; 8u;1ed;;&az;1at;1;A;;1; ;5;;+1;9;;,;t;;.3a;62;;/b;z;;12n;4s;1;.b;2;;2;;1;11;c;;4d;s;;63;f;;87;j;;A;;3; e;23;;F9q;ys;1;Tf;;;I7e;i;1;A;6;;B4i;18i;;C73;pl;;D5j;hm;;Etd;3s;3; b;13;;Rb;r;;U;5;;F14y;6pf;;G19;75;;H2k;1l;1;S5;i;;Ijt;7z;3; 2;d;;A27;4c;;C2j;1x;1; d;1q;;J15;35;;K14;97;;L26;3f;2; 16;m;;P6;;;M97;k2;5;1q;5;;2j;;;3b;1;;5b;1;;75;;;N7e;yo;;Okj;b2;2;Hg;1z;;T2c;43;2;A19;;;O8;;;P5g;ug;;Qc;10;;R22;3t;1;F5;;;S;;4; 1gt;19m;;.id;cc;;?3e;1j;;T;;1;P;6;;T6p;mg;;U2m;4p;5;B6;;;L5;;;Mb;;;Na;;;Sc;1;;V1t;bw;;X6;p;;c;9;;f41p;g1l;1;M;;1;.h;4;;h2bf;1d;2;A;6;;B;5;;ò;5;;Sa0rq;igm;12; eo;1io;;&1u;45;1;Wi;6;;'5;f;;,1;m;;.jg;a2;4;Bk;11;;Mi;16;1;.i;16;1;A5;;;Ob;1j;;T1u;19;1;.1t;19;1;M2;f;;0;5;;112;2i;;38;13;;4f;z;;5c;r;;6i;z;;88;l;;A1k2;py;4; 24;8x;;O;7;;S2u;4l;2;Eo;3;;I8;;;T61;3b;1;Bb;s;;B3w;9h;;Cop;bw;4;A3y;15;1; 8;y;;P4;g;;R1f;25;2;Ar;;;I6;;;T5;11;;D8g;t4;;E13z;qv;8; s;1p;;C4i;ex;4;Ab;;;I5;;;Op;;;U5;;;Dc;y;;I;;1;F9;;;M;;1;I5;;;O1p;3n;;Sg;t;1;Ac;3;;U1;8;;F6b;a1;3;&7;;;H7;;;O;;1; ;7;;G2x;6k;1;Dh;6;;H9e;4f;3;2a;o;;F;8;;L1;8;;I10x;b0;3; p;5r;;A;;1;A3;o;;I;;1; ;6;;K;;2; f;v;;S4;q;;L8p;ai;4;1a;;;A3f;r;1; 3;m;;I;;1; 2;e;;O;;1; 1;b;;Mc8;qb;4;A2w;u;1; 1;s;;E;;1;Rj;;;Of;1;;U;;1;R6;;;N;;5;A1p;h;;C;;2;A9;;;Cg;2;;E1j;r;;Fb;2;;O7l;1;;Omw;fs;5;3;5;;A41;1b;1;I5;f;;En;26;;S16;6y;1;U7;;;Va;11;;P2on;4x2;7;A1kk;26j;7;Ae;2;;Cj;;;D34;;;Mw;;;Nq;;;R38;;;S6;;;E4n;n;1; 6;i;;Ijd;214;6;Cv;;;D6;;;K5;;;Nc;;;R6;;;T5;;;L1k;2g;2;I6;;;O7;;;O1d;g;;U18;3;;Y6;;;R4u;by;1;U9;1;;Sh1;1sg;2;Hr;2i;1;Rc;2;;Wd;1;;Tr5;i5;a;A5f;z;1; 7;s;;B5;j;;C8;s;;Dp;3m;;F5;q;;Iv;26;1;N6;;;L8;p;;Ml;1e;;Sg;1h;;V5;11;;U;;8;Bx;3;;Gr;;;L4d;2g;;Mz;;;N1u;1;;P19;3;;Re;;;Sm;;;V8j;13z;;Wkq;3a;1;R4;p;;X1m;10;1; 6;j;;hcv2;18;1;i3bz;m;1;g;;1;a;;1;o;8;;p17ko;33;2;aszq;v;1;rp9;9;1;á;7;;i1ug;11;1;o7;s;1;n7;;;tvt0;2y;1;iqx;e;1;c71;b;1;i;9;;²;5;;T4c0k;i7;5;ah0d;3e;2;i39c;16;2;g;;1;a5;;;sm;8;1;c;8;;v1z;o;1;e1h;h;1;s;h;;ekci;28;1;a28x;t;1;mks;7;1;p;7;;ugjk;1o;2;a36;f;1;i1;e;;i;;2;l;8;;r;9;;à1;o;;ó;8;;U3f8b;bz1;l;.nqp;4f;1;N8j;i;1;.8c;i;1;C;d;;1;;2; l;;;05;;;25n;2s;1;3s;1t;;L4v;p;1;T;9;;M9i;1r;1;Na;z;;N5se;d7;4;C38;k;1;L;5;;D1q;2c;2; 5;;;P1h;2;;Ii2;k;1;N;5;;Rq;15;1; 5;;;Pk5;1i;1;D;;1;A;9;;a4;4p;;b37;7g;2;ap;9;;iv;b;;dn;1g;;g16e;4l;1;l;1z;;l3j;1hx;2;ig;5;;yi;1;;m17;a7;1; 5;;;n110z;2yd;o; 7;o;;a;;4;b;;1;r;6;;c;7;;s1;9;;u;g;;b5;1r;;ce;bl;;d1x;132;;e1b;2u;1;s19;2;;f3;2q;;g4;o;;h6;w;;i10s9;c0;2;d2;c;;n4;42;;j;7;;k5;6g;;l4;2x;;ma;1f;;n1;u;;o6;19;;p2;2c;;q;6;;r6;58;;s7;3z;;t6;4p;;u6;z;;v1;8;;w1;z;;p1l;13n;1;l9;13;1;a;;1;y6;;;r1qh;29a;3;a5g;10;;ed;;;u1ga;ji;1;k;l;;s1mm;5o;4; ;a;;h7;18;;s1;a;;tx;2j;1;rm;1;;tpt;5b;3;h2;a;;n2;m;;rh;1j;;x3;h;;z3t;m3;;í5;t;;V1s0x;6l;1;Iy9;18;1;Ii;z;;W4rsb;2zw;2;Pqcf;2k7;1;:muf;2io;5;A;;3;E19;q;;N3u;2m;;U;;1;T;;1;O;;1; t;4;;E3k;5k;;I;;1;N1l;37;;N1ik;5p;2;M6;o;;Pcx;3q;1;Oaj;3c;1;L2;1k;;R2gv;h9;1;F;;1;C;;2;N5;;;U1d;e;;i1nqn;9a;1;k1ao8;88;1;i1a8y;86;1;E15;9;1;l;;1;i1;8;;X1ku;6vt;b;A;;1; ;a;;I;;2;Vj;6;;Xa;2;;X;;2; e;t;;Y;a;;a34;7;;c;;1; c;3;;ea2;9;;h2x;1;;ibb;d;;oq;5;;u1f;5;;yw;5;1;z;5;;Yizx;75;1;o8i1;1g;1;u6o1;14;1;n2c6;h;1;g2bw;h;1;g;9;;[1wo;e4;5;A5;l;;am;1m;;ef;3b;;iq;1c;;s5t;i;1;];;1; ;5;;_2ivy;87v;2;_2is8;87d;1;i;5;;e;5;;`4y;1c;3;a1;e;;i;6;;u;6;;acr5;1zx0e;a; 2g9;1pg;;cs1;7dun;1;a39;i91;1;b5;;;lpx;6e00;1;gy;5gh;1;uh;5;1;a;5;;m9e;1pb5;1;a3p;kid;1;r;;1; i;;;n2hx;67ie;1;d100;1ip;2; zd;8a;;ě6;;;pcr;3p7z;2;p9c;33i8;1;e1q;rmr;1;a1i;o2f;1;r14;ell;1; b;2;;rp;h7;1;t7;;;qm;4xk;1;ul;4x8;1;e6;1bt;1;l5;;;r2ag;hboi;1;t1pj;a56i;1;í6;;;ugz;2umm;1;si;142;1;s;;1;i;;1;e;7;;v44;3c48;1;ua;2r;1;ta;1;;d1g76f;66t;3;a5m1w;4p;1;s23w;2d;1; ;2a;;ehqlw;50g;3;i214;a;1;ny;8;1;e;7;;muma;27;1; ;18;;rbwy;4be;1; 1;4an;;ihp8n;pl;2;chqx;n;1;hgn;d;1; 1;a;;e6na;97;1;s292;p;1;e26y;p;2;m;7;;r;5;;eamb;19kiy;a;c54;u5d;1;o3k;ik3;1;n2h;efc;1;o2e;ef1;1;m2e;ees;1;i28;c20;1;a6;;;d11c;3agt;1;ixz;2mju;1;ç5;;;ef;19q;1;w8;;;lhp;3u45;2;i16;6t9;1;tn;4od;1;ek;4dq;1;i9;;;l1b;30h;2;ag;z;1; a;;;o6;;;mbi;2b9m;1;p3w;vho;1;eg;zy;1;zg;;;n1ah;7v81;1;taf;1tlz;1;e1o;6u8;1;n6;y;1;d6;;;sdh;2jw2;2;a;;1;y;6;;t5v;1kiq;5;a3a;fq7;2; h;;;s6;;;ef;ou;1; d;;;io;12ti;1;mo;12tc;1;ao;12ta;1;r5;;;rm;mu;1;u;;1;t6;;;é5;;;t4k;evu;1;ea;1at;1; 5;;;u3k5;95;1;p19a;2s;1; ;m;;w2m;x;;f23b19;1gf;4; 1i;4n;;B;6;;M8;3j;;y1s;b;1;i;b;;hwsqx;2c9q;8; 5a;ul;;,;6;;23;e;;C5;19;;a65z7;nt;1;u1z1;1y;1;tz;1l;1;b7;;;e4wz2;51l;1;igc2;2oh;1;r2c;2o7;1;a17;1;;o7n0b;1uhq;4;m19qu;26h;2;a1g1;206;1; 9;;;mm;1p;;n1s6;yki;9;ba;;;dh;;;e195;3xx;3; 7;;;de;;;y14m;2;;ga;;;ig;1;;j7;;;k4v;;;vg;;;z5;;;ru3i;9c;1;sfaa;2j;1; e;1w;;u1vw9;swj;1;r3j;svc;;t38;62;1;t;;1;p;;1;:x;d;;i6tm;11lzu;6;ec;r;1;r9;;;n3zn;pmya;2;cb3;2qm1;1;ei;336;1;p5;g;1;u5;;;ffq;1yxn;2;l2d;jv6;1;u1n;ggw;1;êb;;;o8x;kvp;1;ab;2;;r4i;j0d;1; c;1;;sv7;1muz;1; l0;4h;;t84;825;2;:5;;;s2g;1o;1;y;a;;u1b;a;;k4x2c;5g;1;uxp;f;1; 3;f;;l1t89f;129;3;ck;8;1;d;6;;sl;5;1; ;5;;vz;a;1;a2;a;;m28vzu;2dl;b; 1s;9g;;,1;b;;D3;g;;R14;42;;b;;1;o4;h;;dk;14;;em1xm;an;1;i2z;i;1;n3;h;;f6;i;;p15;6c;;t;;1;v;a;;×;9;;n1gbc2;3j4;9; b8;1gs;;';;1;a6;;;+1;b;;V8;w;;W3;k;;d15;1y;1; h;2;;tk;18;;x3;c;;×1;b;;o2egz;lfct;a;bch;1r8p;1;ěa;;;c3p;j6u;1;hc;dh;1;oa;1;;d2p;84d;2;d24;6hk;1;yc;;;i7;2u;1; 5;;;fo7;334a;1; b5;5m;;g13;ka;1;gm;12;1;ii;;;l9d;1sk0;1;va;;;n2aq1;1h79;o; 54;7tq;;';5;;,;8;;/2;4n;;a1;r;;b;he;;c26d;11o;2;h;5;;o2;10v;;d1;t;;e280k;hx;2;i;m;;r8;4a;;f;a;;g2t;dzf;;i4;fh;;la5;qfu;;m3;w;;n;f;;o4;67;;r1;1n;;s5;1bk;;t9;k0;;u1;23;;w1;23;;y1;1b;;z;7;;–;9;;rn5;3lct;4; 4q;3g;;a1y;8xj;1;n19;46b;1;ž5;;;g5w;194t;1;a5n;18pg;1;n5k;18fm;1;i53;14gf;1;z3n;ptv;1;a2p;l8j;1;ç7;;;i3v;lt4;1;g3g;k4l;1;e6;;;t4e;6fp;1;rs;g;1;s;b;;uaf;1sl3;1;i1p;e;;r1v2bo;230;4; 1r;77;;cb;s;;f1c;5k;;m;;1;s;6;;s49wfk;3yt;e; d4;g8;;';;1;m8;;;,1;d;;.;;1;p3;d;;?;5;;R5;k;;f;;2;ec;;;o9;;;ildgs;cd;1;c2ha;1j;1;hb;1i;;l2kgl;1t;1; ;6;;pf1l5;dx;3;27;o;;34;n;;iwge;19;1; 1;8;;r2e;q;1;g;j;;s1k;z;1;h5;l;;tp05e;jj;1; 6;j;;v;;3;ak;1;;e15;;;i5;;;t1q8cx;1tc;3;'h;6;1;S;5;;A1;c;;S3;3e;;u5eq5;bhly;c; a1;n;;.a;;;bij;4i;2;e9;1v;;l;7;;fq;8;;k8o;33;1;i6;13;;m2a;5ox;1; h;3;;n2zgo;97g3;3;a4v2;9vg;4; 3a;4;;n4ko;pp;5;a2;b;;c;e;;n4;ey;;s2;44;;t;47;;r51;q2;1;y4v;2;;s8;sq;1; 6;;;e2w;d43;2; x;;;i7;;;i2sqd;18zx;7;d6u;3ai;1;i5r;j;1;o;7;;g;;1;n;8;;l123;1s;1;l;o;;m35;wq;1;o2v;6;;n3a;13zt;;sc8;v;1;s;k;;vm8n;8u;1;o11;2d;1;ck;3;;r1rz;eu0;5;afj;y;;e5n;l;;in0;s;1;p1;c;;lbu;1w;;o5n;l;;s27s5;1vn;3; p;4g;;h5;c8;;u1nm;l8;2; ;5;;rgh;ip;3;er;9;;pfe;ie;3;a46;2b;;e9l;3l;;il;;;y5;;;t3u5;1no;4;a4;f;;m;z;;s;5;;td;1i1;;v4i;f;;w3z;g;1;a;9;;x;;b; 26;ld;;';b;;,1;h;;.2;b;;8u;56;;F2;b;;k;11;;l1;9;;md;z;1;a6;;;t2;c;;y5n;i;1; ;b;;y875t;e5;1;l1;e;;{7q;z;1;a;7;;|6v;r;1;a1;c;;£8l1;b5;2;12v5;24;1;81e;u;1;m2;a;;82o;8n;;«5c;o;1;i;5;;À2;a;;Ád;18;;Å2;s;;Æ2;h;;Én;44;;Î;5;;Ò;a;;Ó2;1g;;Öa;1e;;Ü6;r;;à4;1h;;á7;m;;å1;a;;æ2;u;;è1;8;;é66;dx;3;gd;2;;l7;1y;1; 5;;;t51;37;3;a5;28;;o;g;;u2;h;;ö2;j;;ü;;1; c;1;;Ā;e;;ā2;13;;ī;7;;İ2;t;;Ō;x;;ō;36;;œ1;8;;ɑ;5;;ʿ;8;;Ε;5;;Ω2;g;;α1h;hs;;ε4;1x;;ω8;23;;ℓ;i;;∞4;w;;");

    private Articles() {
    }

    /**
     * Gets the article.
     *
     * <p>It is assumed that {@code word} is a complete word; in some cases word-prefixes may result in a
     * differing classification than complete words. If you wish to classify an incomplete word (a prefix), append
     * a non-word, non-space character such as the underscore ({@code _}) as a placeholder for further letters.</p>
     *
     * @param word the word to test
     * @return {@code "a"} or {@code "an"}
     */
    public static String get(final String word) {
        return find(word).article();
    }

    /**
     * Gets the article.
     *
     * <p>It is assumed that {@code word} is a complete word; in some cases word-prefixes may result in a
     * differing classification than complete words. If you wish to classify an incomplete word (a prefix), append
     * a non-word, non-space character such as the underscore ({@code _}) as a placeholder for further letters.</p>
     *
     * @param word the word to test
     * @return {@code word} prepended with {@code "a"} or {@code "an"}
     */
    public static String prepend(final String word) {
        return find(word).prefixed();
    }

    // https://github.com/EamonNerbonne/a-vs-an/blob/master/A-vs-An/A-vs-An-DotNet/Internals/WordQuery.cs
    private static Result find(final String word) {
        Node node = NODE;
        int depth = 0;
        Ratio ratio = node.ratio;
        final int length = word.length();
        while (true) {
            if (depth >= length) {
                return new Result(ratio, word);
            } else {
                final char c = word.charAt(depth);
                if (c == '"' || c == '‘' || c == '’' || c == '“' || c == '”' || c == '$' || c == '\'' || c == '-' || c == '(') {
                    depth++;
                } else {
                    break;
                }
            }
        }

        while(true) {
            if(node.children == null) {
                break;
            }

            final char c = depth == length ? ' ' : word.charAt(depth);
            int candidateIdx = node.children.length - 1;
            int start = 0;

            while(candidateIdx != start) {
                final int midpoint = (candidateIdx + start) >> 1;
                if(node.children[midpoint].c < c) {
                    start = midpoint + 1;
                } else {
                    candidateIdx = midpoint;
                }
            }

            if(node.children[candidateIdx].c != c) {
                break;
            }

            node = node.children[candidateIdx];
            if(node.ratio.occurrence != 0) {
                ratio = node.ratio;
            }

            depth++;
            if(depth > length) {
                break;
            }
        }

        return new Result(ratio, word);
    }

    // https://github.com/EamonNerbonne/a-vs-an/blob/master/A-vs-An/A-vs-An-DotNet/Internals/Node.cs
    private static class Node {
        final Ratio ratio;
        final Node[] children;
        final char c;

        Node(final Ratio ratio, final Node[] children, final char c) {
            this.ratio = ratio;
            this.children = children;
            this.c = c;
        }
    }

    // https://github.com/EamonNerbonne/a-vs-an/blob/master/A-vs-An/A-vs-An-DotNet/Internals/NodeDeserializer.cs
    private static final class NodeParser {
        private int cursor = 0;

        Node parse(final String string) {
            return this.parse0(string, '\u0000');
        }

        private Node parse0(final String string, final char cc) {
            final Ratio ratio = new Ratio(this.parseInt(string), this.parseInt(string));

            final int childCount = this.parseInt(string);
            final Node[] children;
            if (childCount != 0) {
                children = new Node[childCount];
                for (int i = 0; i < childCount; i++) {
                    final char c = string.charAt(this.cursor++);
                    children[i] = this.parse0(string, c);
                }
            } else {
                children = null;
            }

            return new Node(ratio, children, cc);
        }

        private int parseInt(final String string) {
            int result = 0;
            char c = string.charAt(this.cursor++);
            while(c != ';') {
                result = result * 36;
                result += c - (c >= 'a' ? 'a' - 10 : '0');
                c = string.charAt(this.cursor++);
            }
            return result;
        }
    }

    // https://github.com/EamonNerbonne/a-vs-an/blob/master/A-vs-An/A-vs-An-DotNet/Internals/Ratio.cs
    private static final class Ratio {
        final int occurrence;
        final int difference;

        Ratio(final int aCount, final int anCount) {
            this.occurrence = aCount + anCount;
            this.difference = aCount - anCount;
        }
    }

    // https://github.com/EamonNerbonne/a-vs-an/blob/master/A-vs-An/A-vs-An-DotNet/AvsAn.cs#L27
    public static final class Result {
        private final Ratio ratio;
        private final String word;

        Result(final Ratio ratio, final String word) {
            this.ratio = ratio;
            this.word = word;
        }

        String article() {
            return this.ratio.difference >= 0 ? "a" : "an";
        }

        String prefixed() {
            return this.article() + " " + this.word;
        }
    }
}
