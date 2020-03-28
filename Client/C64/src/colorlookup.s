; -------------------------------------------------------------------------
; Rogue Color Lookup Table

.export color_table

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
; Refer to Tile Types.xlsx

color_table:
    .byte COLOR_BLACK    ; 0    Empty Space
    .byte COLOR_GREY2    ; 1    Brick Wall	
    .byte COLOR_GREY1    ; 2	Dirt 	
    .byte COLOR_GREY2    ; 3	Closed Door 	
    .byte COLOR_GREY2    ; 4	Broken Wall 	
    .byte COLOR_WHITE    ; 5	Alternate Stairs Down 	
    .byte COLOR_WHITE    ; 6    Closed Door Big
    .byte COLOR_WHITE    ; 7	Open Door
    .byte COLOR_GREY3    ; 8	Stairs Down	
    .byte COLOR_GREY3    ; 9    Stairs Up
    .byte COLOR_GREY1    ; 10	Debris 	
    .byte COLOR_GREY2    ; 11	Squares 	
    .byte COLOR_GREY2    ; 12	Big Wall 	
    .byte COLOR_GREY2    ; 13	Open Door? 	
    .byte COLOR_GREY2    ; 14	Secret Door?	
    .byte COLOR_RED      ; 15	Lava 	
    .byte COLOR_BLUE     ; 16	Water 	
    .byte COLOR_WHITE    ; 17	?	
    .byte COLOR_LIGHTBLUE; 18   Portal	 	
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
    .byte COLOR_WHITE    ; 32	Punctuation... 	
    .byte COLOR_WHITE    ; 33	 	
    .byte COLOR_WHITE    ; 34    	
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
    .byte COLOR_WHITE    ; 64  ASCII Letters...	 	
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
    .byte COLOR_LIGHTRED ; 99	 	Demon
    .byte COLOR_CYAN     ; 100	 	Spectre
    .byte COLOR_WHITE    ; 101	 	Ghost
    .byte COLOR_GREEN    ; 102	 	Frog Thing
    .byte COLOR_GREEN    ; 103      Slime	 	
    .byte COLOR_LIGHTGREEN ; 104    Zombie	 	
    .byte COLOR_ORANGE   ; 105	 	Golem
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
    .byte COLOR_WHITE    ; 127	 	
    .byte COLOR_WHITE    ; 128	 	Generic Player
    .byte COLOR_WHITE    ; 129	 	Mage
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
    .byte COLOR_LIGHTBLUE; 192   Sword	 	
    .byte COLOR_GREY3    ; 193	 Shield	
    .byte COLOR_ORANGE   ; 194	 Bow	
    .byte COLOR_ORANGE   ; 195	 Arrow	
    .byte COLOR_VIOLET   ; 196	 Potion	
    .byte COLOR_CYAN     ; 197	 Gem	
    .byte COLOR_WHITE    ; 198	 Note	
    .byte COLOR_BROWN    ; 199	 Chest  (Closed)
    .byte COLOR_WHITE    ; 200	 Crook	
    .byte COLOR_YELLOW   ; 201	 Gold	
    .byte COLOR_WHITE    ; 202   ?	 	
    .byte COLOR_BROWN    ; 203	 Rope	
    .byte COLOR_YELLOW   ; 204	 Key	
    .byte COLOR_BROWN    ; 205   Sign	 	
    .byte COLOR_WHITE    ; 206	 	
    .byte COLOR_WHITE    ; 207	 	
    .byte COLOR_WHITE    ; 208	 	
    .byte COLOR_BROWN    ; 209   Chest (Open but empty)	 	
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
                              