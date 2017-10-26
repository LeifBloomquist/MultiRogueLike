; -------------------------------------------------------------------------
; Rogue Color Lookup Table

COLOR_BLACK	     = 0	
COLOR_WHITE	     = 1	
COLOR_RED	     = 2	
COLOR_CYAN	     = 3	
COLOR_VIOLET	 = 4	
COLOR_GREEN	     = 5	
COLOR_BLUE	     = 6	
COLOR_YELLOW	 = 7	
COLOR_ORANGE	 = 8	
COLOR_BROWN	     = 9
COLOR_LIGHTRED	 = 10	
COLOR_GREY1	     = 11	
COLOR_GREY2	     = 12	
COLOR_LIGHTGREEN = 13	
COLOR_LIGHTBLUE	 = 14	
COLOR_GREY3      = 15	

; Rather than have the server send an entire extra screen's worth of color data, we use this lookup table to 
; map character codes to color codes.

colortable:

    .byte COLOR_WHITE    ; 0    Empty Space
    .byte COLOR_GREY2    ; 1    Brick Wall	
    .byte COLOR_WHITE    ; 2	 	
    .byte COLOR_WHITE    ; 3	 	
    .byte COLOR_WHITE    ; 4	 	
    .byte COLOR_WHITE    ; 5	 	
    .byte COLOR_WHITE    ; 6	 	
    .byte COLOR_WHITE    ; 7	 	
    .byte COLOR_WHITE    ; 8	 	
    .byte COLOR_WHITE    ; 9	 	
    .byte COLOR_WHITE    ; 10	 	
    .byte COLOR_WHITE    ; 11	 	
    .byte COLOR_WHITE    ; 12	 	
    .byte COLOR_WHITE    ; 13	 	
    .byte COLOR_WHITE    ; 14	 	
    .byte COLOR_WHITE    ; 15	 	
    .byte COLOR_WHITE    ; 16	 	
    .byte COLOR_WHITE    ; 17	 	
    .byte COLOR_WHITE    ; 18	 	
    .byte COLOR_WHITE    ; 19	 	
    .byte COLOR_WHITE    ; 20	 	
    .byte COLOR_WHITE    ; 21	 	
    .byte COLOR_WHITE    ; 22	 	
    .byte COLOR_WHITE    ; 23	 	
    .byte COLOR_WHITE    ; 24	 	
    .byte COLOR_WHITE    ; 25	 	
    .byte COLOR_WHITE    ; 26	 	
    .byte COLOR_WHITE    ; 27	 	
    .byte COLOR_WHITE    ; 28	 	
    .byte COLOR_WHITE    ; 29	 	
    .byte COLOR_WHITE    ; 30	 	
    .byte COLOR_WHITE    ; 31	 	
    .byte COLOR_WHITE    ; 32	 	
    .byte COLOR_WHITE    ; 33	 	
    .byte COLOR_GREY2    ; 34   Thick Brick Wall 	
    .byte COLOR_WHITE    ; 35	 	
    .byte COLOR_WHITE    ; 36	 	
    .byte COLOR_WHITE    ; 37	 	
    .byte COLOR_WHITE    ; 38	 	
    .byte COLOR_WHITE    ; 39	 	
    .byte COLOR_WHITE    ; 40	 	
    .byte COLOR_WHITE    ; 41	 	
    .byte COLOR_WHITE    ; 42	 	
    .byte COLOR_WHITE    ; 43	 	
    .byte COLOR_WHITE    ; 44	 	
    .byte COLOR_WHITE    ; 45	 	
    .byte COLOR_WHITE    ; 46	 	
    .byte COLOR_WHITE    ; 47	 	
    .byte COLOR_WHITE    ; 48	 	
    .byte COLOR_WHITE    ; 49	 	
    .byte COLOR_WHITE    ; 50	 	
    .byte COLOR_WHITE    ; 51	 	
    .byte COLOR_WHITE    ; 52	 	
    .byte COLOR_WHITE    ; 53	 	
    .byte COLOR_WHITE    ; 54	 	
    .byte COLOR_WHITE    ; 55	 	
    .byte COLOR_WHITE    ; 56	 	
    .byte COLOR_WHITE    ; 57	 	
    .byte COLOR_WHITE    ; 58	 	
    .byte COLOR_WHITE    ; 59	 	
    .byte COLOR_WHITE    ; 60	 	
    .byte COLOR_WHITE    ; 61	 	
    .byte COLOR_WHITE    ; 62	 	
    .byte COLOR_WHITE    ; 63	 	
    .byte COLOR_WHITE    ; 64	 	
    .byte COLOR_WHITE    ; 65	 	
    .byte COLOR_WHITE    ; 66	 	
    .byte COLOR_WHITE    ; 67	 	
    .byte COLOR_WHITE    ; 68	 	
    .byte COLOR_WHITE    ; 69	 	
    .byte COLOR_WHITE    ; 70	 	
    .byte COLOR_WHITE    ; 71	 	
    .byte COLOR_WHITE    ; 72	 	
    .byte COLOR_WHITE    ; 73	 	
    .byte COLOR_WHITE    ; 74	 	
    .byte COLOR_WHITE    ; 75	 	
    .byte COLOR_WHITE    ; 76	 	
    .byte COLOR_WHITE    ; 77	 	
    .byte COLOR_WHITE    ; 78	 	
    .byte COLOR_WHITE    ; 79	 	
    .byte COLOR_WHITE    ; 80	 	
    .byte COLOR_WHITE    ; 81	 	
    .byte COLOR_WHITE    ; 82	 	
    .byte COLOR_WHITE    ; 83	 	
    .byte COLOR_WHITE    ; 84	 	
    .byte COLOR_WHITE    ; 85	 	
    .byte COLOR_WHITE    ; 86	 	
    .byte COLOR_WHITE    ; 87	 	
    .byte COLOR_WHITE    ; 88	 	
    .byte COLOR_WHITE    ; 89	 	
    .byte COLOR_WHITE    ; 90	 	
    .byte COLOR_WHITE    ; 91	 	
    .byte COLOR_WHITE    ; 92	 	
    .byte COLOR_WHITE    ; 93	 	
    .byte COLOR_WHITE    ; 94	 	
    .byte COLOR_WHITE    ; 95	 	
    .byte COLOR_RED      ; 96	 	Spider
    .byte COLOR_WHITE    ; 97	 	Skeleton
    .byte COLOR_BROWN    ; 98	    Bat	
    .byte COLOR_WHITE    ; 99	 	
    .byte COLOR_WHITE    ; 100	 	
    .byte COLOR_WHITE    ; 101	 	
    .byte COLOR_WHITE    ; 102	 	
    .byte COLOR_GREEN    ; 103      Slime  	 	
    .byte COLOR_LIGHTGREEN ; 104    Zombie	 	
    .byte COLOR_WHITE    ; 105	 	
    .byte COLOR_WHITE    ; 106	 	
    .byte COLOR_WHITE    ; 107	 	
    .byte COLOR_WHITE    ; 108	 	
    .byte COLOR_WHITE    ; 109	 	
    .byte COLOR_WHITE    ; 110	 	
    .byte COLOR_WHITE    ; 111	 	
    .byte COLOR_WHITE    ; 112	 	
    .byte COLOR_WHITE    ; 113	 	
    .byte COLOR_WHITE    ; 114	 	
    .byte COLOR_WHITE    ; 115	 	
    .byte COLOR_WHITE    ; 116	 	
    .byte COLOR_WHITE    ; 117	 	
    .byte COLOR_WHITE    ; 118	 	
    .byte COLOR_WHITE    ; 119	 	
    .byte COLOR_WHITE    ; 120	 	
    .byte COLOR_WHITE    ; 121	 	
    .byte COLOR_WHITE    ; 122	 	
    .byte COLOR_WHITE    ; 123	 	
    .byte COLOR_WHITE    ; 124	 	
    .byte COLOR_WHITE    ; 125	 	
    .byte COLOR_WHITE    ; 126	 	
    .byte COLOR_WHITE    ; 127	 	Generic Player
    .byte COLOR_WHITE    ; 128	 	
    .byte COLOR_WHITE    ; 129	 	
    .byte COLOR_WHITE    ; 130	 	
    .byte COLOR_WHITE    ; 131	 	
    .byte COLOR_WHITE    ; 132	 	
    .byte COLOR_WHITE    ; 133	 	
    .byte COLOR_WHITE    ; 134	 	
    .byte COLOR_WHITE    ; 135	 	
    .byte COLOR_WHITE    ; 136	 	
    .byte COLOR_WHITE    ; 137	 	
    .byte COLOR_WHITE    ; 138	 	
    .byte COLOR_WHITE    ; 139	 	
    .byte COLOR_WHITE    ; 140	 	
    .byte COLOR_WHITE    ; 141	 	
    .byte COLOR_WHITE    ; 142	 	
    .byte COLOR_WHITE    ; 143	 	
    .byte COLOR_WHITE    ; 144	 	
    .byte COLOR_WHITE    ; 145	 	
    .byte COLOR_WHITE    ; 146	 	
    .byte COLOR_WHITE    ; 147	 	
    .byte COLOR_WHITE    ; 148	 	
    .byte COLOR_WHITE    ; 149	 	
    .byte COLOR_WHITE    ; 150	 	
    .byte COLOR_WHITE    ; 151	 	
    .byte COLOR_WHITE    ; 152	 	
    .byte COLOR_WHITE    ; 153	 	
    .byte COLOR_WHITE    ; 154	 	
    .byte COLOR_WHITE    ; 155	 	
    .byte COLOR_WHITE    ; 156	 	
    .byte COLOR_WHITE    ; 157	 	
    .byte COLOR_WHITE    ; 158	 	
    .byte COLOR_WHITE    ; 159	 	
    .byte COLOR_WHITE    ; 160	 	
    .byte COLOR_WHITE    ; 161	 	
    .byte COLOR_WHITE    ; 162	 	
    .byte COLOR_WHITE    ; 163	 	
    .byte COLOR_WHITE    ; 164	 	
    .byte COLOR_WHITE    ; 165	 	
    .byte COLOR_WHITE    ; 166	 	
    .byte COLOR_WHITE    ; 167	 	
    .byte COLOR_WHITE    ; 168	 	
    .byte COLOR_WHITE    ; 169	 	
    .byte COLOR_WHITE    ; 170	 	
    .byte COLOR_WHITE    ; 171	 	
    .byte COLOR_WHITE    ; 172	 	
    .byte COLOR_WHITE    ; 173	 	
    .byte COLOR_WHITE    ; 174	 	
    .byte COLOR_WHITE    ; 175	 	
    .byte COLOR_WHITE    ; 176	 	
    .byte COLOR_WHITE    ; 177	 	
    .byte COLOR_WHITE    ; 178	 	
    .byte COLOR_WHITE    ; 179	 	
    .byte COLOR_WHITE    ; 180	 	
    .byte COLOR_WHITE    ; 181	 	
    .byte COLOR_WHITE    ; 182	 	
    .byte COLOR_WHITE    ; 183	 	
    .byte COLOR_WHITE    ; 184	 	
    .byte COLOR_WHITE    ; 185	 	
    .byte COLOR_WHITE    ; 186	 	
    .byte COLOR_WHITE    ; 187	 	
    .byte COLOR_WHITE    ; 188	 	
    .byte COLOR_WHITE    ; 189	 	
    .byte COLOR_WHITE    ; 190	 	
    .byte COLOR_WHITE    ; 191	 	
    .byte COLOR_WHITE    ; 192	 	
    .byte COLOR_WHITE    ; 193	 	
    .byte COLOR_WHITE    ; 194	 	
    .byte COLOR_WHITE    ; 195	 	
    .byte COLOR_WHITE    ; 196	 	
    .byte COLOR_WHITE    ; 197	 	
    .byte COLOR_WHITE    ; 198	 	
    .byte COLOR_WHITE    ; 199	 	
    .byte COLOR_WHITE    ; 200	 	
    .byte COLOR_WHITE    ; 201	 	
    .byte COLOR_WHITE    ; 202	 	
    .byte COLOR_WHITE    ; 203	 	
    .byte COLOR_WHITE    ; 204	 	
    .byte COLOR_WHITE    ; 205	 	
    .byte COLOR_WHITE    ; 206	 	
    .byte COLOR_WHITE    ; 207	 	
    .byte COLOR_WHITE    ; 208	 	
    .byte COLOR_WHITE    ; 209	 	
    .byte COLOR_WHITE    ; 210	 	
    .byte COLOR_WHITE    ; 211	 	
    .byte COLOR_WHITE    ; 212	 	
    .byte COLOR_WHITE    ; 213	 	
    .byte COLOR_WHITE    ; 214	 	
    .byte COLOR_WHITE    ; 215	 	
    .byte COLOR_WHITE    ; 216	 	
    .byte COLOR_WHITE    ; 217	 	
    .byte COLOR_WHITE    ; 218	 	
    .byte COLOR_WHITE    ; 219	 	
    .byte COLOR_WHITE    ; 220	 	
    .byte COLOR_WHITE    ; 221	 	
    .byte COLOR_WHITE    ; 222	 	
    .byte COLOR_WHITE    ; 223	 	
    .byte COLOR_WHITE    ; 224	 	
    .byte COLOR_WHITE    ; 225	 	
    .byte COLOR_WHITE    ; 226	 	
    .byte COLOR_WHITE    ; 227	 	
    .byte COLOR_WHITE    ; 228	 	
    .byte COLOR_WHITE    ; 229	 	
    .byte COLOR_WHITE    ; 230	 	
    .byte COLOR_WHITE    ; 231	 	
    .byte COLOR_WHITE    ; 232	 	
    .byte COLOR_WHITE    ; 233	 	
    .byte COLOR_WHITE    ; 234	 	
    .byte COLOR_WHITE    ; 235	 	
    .byte COLOR_WHITE    ; 236	 	
    .byte COLOR_WHITE    ; 237	 	
    .byte COLOR_WHITE    ; 238	 	
    .byte COLOR_WHITE    ; 239	 	
    .byte COLOR_WHITE    ; 240	 	
    .byte COLOR_WHITE    ; 241	 	
    .byte COLOR_WHITE    ; 242	 	
    .byte COLOR_WHITE    ; 243	 	
    .byte COLOR_WHITE    ; 244	 	
    .byte COLOR_WHITE    ; 245	 	
    .byte COLOR_WHITE    ; 246	 	
    .byte COLOR_WHITE    ; 247	 	
    .byte COLOR_WHITE    ; 248	 	
    .byte COLOR_WHITE    ; 249	 	
    .byte COLOR_WHITE    ; 250	 	
    .byte COLOR_WHITE    ; 251	 	
    .byte COLOR_WHITE    ; 252	 	
    .byte COLOR_WHITE    ; 253	 	
    .byte COLOR_WHITE    ; 254	 	
    .byte COLOR_WHITE    ; 255	 	
                              