package com.kinlhp.moname.addressing.domain.country;

import java.util.Arrays;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.kinlhp.moname.commons.domain.entity.Identifiable;
import com.kinlhp.moname.commons.util.SingletonCollector;

/**
 * Country entity.
 * <p>
 * ISO 3166-1 alpha-3 codes are three-letter country codes defined in ISO 3166-1.
 */
public enum Entity implements Identifiable<String> {

	AFG("004", "AF", new String[]{".af"}, "Afghanistan", "Afghanistan (l')", "Afeganistão"),
	ALB("008", "AL", new String[]{".al"}, "Albania", "Albanie (l')", "Albânia"),
	ATA("010", "AQ", new String[]{".aq"}, "Antarctica", "Antarctique (l')", "Antárctica"),
	DZA("012", "DZ", new String[]{".dz"}, "Algeria", "Algérie (l')", "Argélia"),
	ASM("016", "AS", new String[]{".as"}, "American Samoa", "Samoa américaines (les)", "Samoa Americana"),
	AND("020", "AD", new String[]{".ad"}, "Andorra", "Andorre (l')", "Andorra"),
	AGO("024", "AO", new String[]{".ao"}, "Angola", "Angola (l')", "Angola"),
	ATG("028", "AG", new String[]{".ag"}, "Antigua and Barbuda", "Antigua-et-Barbuda", "Antígua e Barbuda"),
	AZE("031", "AZ", new String[]{".az"}, "Azerbaijan", "Azerbaïdjan (l')", "Azerbaijão"),
	ARG("032", "AR", new String[]{".ar"}, "Argentina", "Argentine (l')", "Argentina"),
	AUS("036", "AU", new String[]{".au"}, "Australia", "Australie (l')", "Austrália"),
	AUT("040", "AT", new String[]{".at"}, "Austria", "Autriche (l')", "Áustria"),
	BHS("044", "BS", new String[]{".bs"}, "Bahamas (the)", "Bahamas (les)", "Bahamas"),
	BHR("048", "BH", new String[]{".bh"}, "Bahrain", "Bahreïn", "Bahrain"),
	BGD("050", "BD", new String[]{".bd"}, "Bangladesh", "Bangladesh (le)", "Bangladesh"),
	ARM("051", "AM", new String[]{".am"}, "Armenia", "Arménie (l')", "Armênia"),
	BRB("052", "BB", new String[]{".bb"}, "Barbados", "Barbade (la)", "Barbados"),
	BEL("056", "BE", new String[]{".be"}, "Belgium", "Belgique (la)", "Bélgica"),
	BMU("060", "BM", new String[]{".bm"}, "Bermuda", "Bermudes (les)", "Bermudas"),
	BTN("064", "BT", new String[]{".bt"}, "Bhutan", "Bhoutan (le)", "Butão"),
	BOL("068", "BO", new String[]{".bo"}, "Bolivia (Plurinational State of)", "Bolivie (État plurinational de)", "Bolívia (Estado Plurinacional da)"),
	BIH("070", "BA", new String[]{".ba"}, "Bosnia and Herzegovina", "Bosnie-Herzégovine (la)", "Bósnia e Herzegovina"),
	BWA("072", "BW", new String[]{".bw"}, "Botswana", "Botswana (le)", "Botswana"),
	BVT("074", "BV", new String[]{}, "Bouvet Island", "Bouvet (l'Île)", "Ilha Bouvet"),
	BRA("076", "BR", new String[]{".br"}, "Brazil", "Brésil (le)", "Brasil"),
	BLZ("084", "BZ", new String[]{".bz"}, "Belize", "Belize (le)", "Belize"),
	IOT("086", "IO", new String[]{".io"}, "British Indian Ocean Territory (the)", "Indien (le Territoire britannique de l'océan)", "Território Britânico do Oceano Índico"),
	SLB("090", "SB", new String[]{".sb"}, "Solomon Islands", "Salomon (les Îles)", "Ilhas Salomão"),
	VGB("092", "VG", new String[]{".vg"}, "Virgin Islands (British)", "Vierges britanniques (les Îles)", "Ilhas Virgens Britânicas"),
	BRN("096", "BN", new String[]{".bn"}, "Brunei Darussalam", "Brunéi Darussalam (le)", "Brunei Darussalam"),
	BGR("100", "BG", new String[]{".bg"}, "Bulgaria", "Bulgarie (la)", "Bulgária"),
	MMR("104", "MM", new String[]{".mm"}, "Myanmar", "Myanmar (le)", "Myanmar"),
	BDI("108", "BI", new String[]{".bi"}, "Burundi", "Burundi (le)", "Burundi"),
	BLR("112", "BY", new String[]{".by"}, "Belarus", "Bélarus (le)", "Bielorrússia"),
	KHM("116", "KH", new String[]{".kh"}, "Cambodia", "Cambodge (le)", "Camboja"),
	CMR("120", "CM", new String[]{".cm"}, "Cameroon", "Cameroun (le)", "Camarões"),
	CAN("124", "CA", new String[]{".ca"}, "Canada", "Canada (le)", "Canadá"),
	CPV("132", "CV", new String[]{".cv"}, "Cabo Verde", "Cabo Verde", "Cabo Verde"),
	CYM("136", "KY", new String[]{".ky"}, "Cayman Islands (the)", "Caïmans (les Îles)", "Ilhas Cayman"),
	CAF("140", "CF", new String[]{".cf"}, "Central African Republic (the)", "République centrafricaine (la)", "República Centro-Africana"),
	LKA("144", "LK", new String[]{".lk"}, "Sri Lanka", "Sri Lanka", "Sri Lanka"),
	TCD("148", "TD", new String[]{".td"}, "Chad", "Tchad (le)", "Chade"),
	CHL("152", "CL", new String[]{".cl"}, "Chile", "Chili (le)", "Chile"),
	CHN("156", "CN", new String[]{".cn"}, "China", "Chine (la)", "China"),
	TWN("158", "TW", new String[]{".tw"}, "Taiwan (Province of China)", "Taïwan (Province de Chine)", "Taiwan (Província da China)"),
	CXR("162", "CX", new String[]{".cx"}, "Christmas Island", "Christmas (l'Île)", "Ilha Christmas"),
	CCK("166", "CC", new String[]{".cc"}, "Cocos (Keeling) Islands (the)", "Cocos (les Îles)/ Keeling (les Îles)", "Ilhas Cocos (Keeling)"),
	COL("170", "CO", new String[]{".co"}, "Colombia", "Colombie (la)", "Colômbia"),
	COM("174", "KM", new String[]{".km"}, "Comoros (the)", "Comores (les)", "Comores"),
	MYT("175", "YT", new String[]{".yt"}, "Mayotte", "Mayotte", "Mayotte"),
	COG("178", "CG", new String[]{".cg"}, "Congo (the)", "Congo (le)", "Congo"),
	COD("180", "CD", new String[]{".cd"}, "Congo (the Democratic Republic of the)", "Congo (la République démocratique du)", "Congo (a República Democrática do)"),
	COK("184", "CK", new String[]{".ck"}, "Cook Islands (the)", "Cook (les Îles)", "Ilhas Cook"),
	CRI("188", "CR", new String[]{".cr"}, "Costa Rica", "Costa Rica (le)", "Costa Rica"),
	HRV("191", "HR", new String[]{".hr"}, "Croatia", "Croatie (la)", "Croácia"),
	CUB("192", "CU", new String[]{".cu"}, "Cuba", "Cuba", "Cuba"),
	CYP("196", "CY", new String[]{".cy"}, "Cyprus", "Chypre", "Chipre"),
	CZE("203", "CZ", new String[]{".cz"}, "Czechia", "Tchéquie (la)", "República Checa"),
	BEN("204", "BJ", new String[]{".bj"}, "Benin", "Bénin (le)", "Benim"),
	DNK("208", "DK", new String[]{".dk"}, "Denmark", "Danemark (le)", "Dinamarca"),
	DMA("212", "DM", new String[]{".dm"}, "Dominica", "Dominique (la)", "Dominica"),
	DOM("214", "DO", new String[]{".do"}, "Dominican Republic (the)", "Dominicaine (la République)", "República Dominicana"),
	ECU("218", "EC", new String[]{".ec"}, "Ecuador", "Équateur (l')", "Equador"),
	SLV("222", "SV", new String[]{".sv"}, "El Salvador", "El Salvador", "El Salvador"),
	GNQ("226", "GQ", new String[]{".gq"}, "Equatorial Guinea", "Guinée équatoriale (la)", "Guiné Equatorial"),
	ETH("231", "ET", new String[]{".et"}, "Ethiopia", "Éthiopie (l')", "Etiópia"),
	ERI("232", "ER", new String[]{".er"}, "Eritrea", "Érythrée (l')", "Eritreia"),
	EST("233", "EE", new String[]{".ee"}, "Estonia", "Estonie (l')", "Estónia"),
	FRO("234", "FO", new String[]{".fo"}, "Faroe Islands (the)", "Féroé (les Îles)", "Ilhas Faroé"),
	FLK("238", "FK", new String[]{".fk"}, "Falkland Islands (the)/Malvinas Islands (the)", "Falkland (les Îles)/Malouines (les Îles)", "Ilhas Falkland/Ilhas Malvinas"),
	SGS("239", "GS", new String[]{".gs"}, "South Georgia and the South Sandwich Islands", "Géorgie du Sud-et-les Îles Sandwich du Sud (la)", "Ilhas Geórgia do Sul e Sandwich do Sul"),
	FJI("242", "FJ", new String[]{".fj"}, "Fiji", "Fidji (les)", "Fiji"),
	FIN("246", "FI", new String[]{".fi"}, "Finland", "Finlande (la)", "Finlândia"),
	ALA("248", "AX", new String[]{".ax"}, "Åland Islands", "Åland(les Îles)", "Ilhas Åland"),
	FRA("250", "FR", new String[]{".fr"}, "France", "France (la)", "França"),
	GUF("254", "GF", new String[]{".gf"}, "French Guiana", "Guyane française (la)", "Guiana Francesa"),
	PYF("258", "PF", new String[]{".pf"}, "French Polynesia", "Polynésie française (la)", "Polinésia Francesa"),
	ATF("260", "TF", new String[]{".tf"}, "French Southern Territories (the)", "Terres australes françaises (les)", "Territórios Franceses do Sul"),
	DJI("262", "DJ", new String[]{".dj"}, "Djibouti", "Djibouti", "Djibouti"),
	GAB("266", "GA", new String[]{".ga"}, "Gabon", "Gabon (le)", "Gabão"),
	GEO("268", "GE", new String[]{".ge"}, "Georgia", "Géorgie (la)", "Geórgia"),
	GMB("270", "GM", new String[]{".gm"}, "Gambia (the)", "Gambie (la)", "Gâmbia"),
	PSE("275", "PS", new String[]{".ps"}, "Palestine, State of", "Palestine, État de", "Palestina, Estado da"),
	DEU("276", "DE", new String[]{".de"}, "Germany", "Allemagne (l')", "Alemanha"),
	GHA("288", "GH", new String[]{".gh"}, "Ghana", "Ghana (le)", "Gana"),
	GIB("292", "GI", new String[]{".gi"}, "Gibraltar", "Gibraltar", "Gibraltar"),
	KIR("296", "KI", new String[]{".ki"}, "Kiribati", "Kiribati", "Kiribati"),
	GRC("300", "GR", new String[]{".gr"}, "Greece", "Grèce (la)", "Grécia"),
	GRL("304", "GL", new String[]{".gl"}, "Greenland", "Groenland (le)", "Gronelândia"),
	GRD("308", "GD", new String[]{".gd"}, "Grenada", "Grenade (la)", "Granada"),
	GLP("312", "GP", new String[]{".gp"}, "Guadeloupe", "Guadeloupe (la)", "Guadalupe"),
	GUM("316", "GU", new String[]{".gu"}, "Guam", "Guam", "Guam"),
	GTM("320", "GT", new String[]{".gt"}, "Guatemala", "Guatemala (le)", "Guatemala"),
	GIN("324", "GN", new String[]{".gn"}, "Guinea", "Guinée (la)", "Guiné"),
	GUY("328", "GY", new String[]{".gy"}, "Guyana", "Guyana (le)", "Guiana"),
	HTI("332", "HT", new String[]{".ht"}, "Haiti", "Haïti", "Haiti"),
	HMD("334", "HM", new String[]{".hm"}, "Heard Island and McDonald Islands", "Heard-et-Îles MacDonald (l'Île)", "Ilha Heard e Ilhas McDonald"),
	VAT("336", "VA", new String[]{".va"}, "Holy See (the)", "Saint-Siège (le)", "Santa Sé (Estado da Cidade do Vaticano)"),
	HND("340", "HN", new String[]{".hn"}, "Honduras", "Honduras (le)", "Honduras"),
	HKG("344", "HK", new String[]{".hk"}, "Hong Kong", "Hong Kong", "Hong Kong"),
	HUN("348", "HU", new String[]{".hu"}, "Hungary", "Hongrie (la)", "Hungria"),
	ISL("352", "IS", new String[]{".is"}, "Iceland", "Islande (l')", "Islândia"),
	IND("356", "IN", new String[]{".in"}, "India", "Inde (l')", "Índia"),
	IDN("360", "ID", new String[]{".id"}, "Indonesia", "Indonésie (l')", "Indonésia"),
	IRN("364", "IR", new String[]{".ir"}, "Iran (Islamic Republic of)", "Iran (République Islamique d')", "Irã (República Islâmica do)"),
	IRQ("368", "IQ", new String[]{".iq"}, "Iraq", "Iraq (l')", "Iraque"),
	IRL("372", "IE", new String[]{".ie"}, "Ireland", "Irlande (l')", "Irlanda"),
	ISR("376", "IL", new String[]{".il"}, "Israel", "Israël", "Israel"),
	ITA("380", "IT", new String[]{".it"}, "Italy", "Italie (l')", "Itália"),
	CIV("384", "CI", new String[]{".ci"}, "Côte d'Ivoire", "Côte d'Ivoire (la)", "Costa do Marfim"),
	JAM("388", "JM", new String[]{".jm"}, "Jamaica", "Jamaïque (la)", "Jamaica"),
	JPN("392", "JP", new String[]{".jp"}, "Japan", "Japon (le)", "Japão"),
	KAZ("398", "KZ", new String[]{".kz"}, "Kazakhstan", "Kazakhstan (le)", "Cazaquistão"),
	JOR("400", "JO", new String[]{".jo"}, "Jordan", "Jordanie (la)", "Jordânia"),
	KEN("404", "KE", new String[]{".ke"}, "Kenya", "Kenya (le)", "Quénia"),
	PRK("408", "KP", new String[]{".kp"}, "Korea (the Democratic People's Republic of)", "Corée (la République populaire démocratique de)", "Coréia (República Popular Democrática da)"),
	KOR("410", "KR", new String[]{".kr"}, "Korea (the Republic of)", "Corée (la République de)", "Coréia (República da)"),
	KWT("414", "KW", new String[]{".kw"}, "Kuwait", "Koweït (le)", "Kuwait"),
	KGZ("417", "KG", new String[]{".kg"}, "Kyrgyzstan", "Kirghizistan (le)", "Quirguistão"),
	LAO("418", "LA", new String[]{".la"}, "Lao People's Democratic Republic (the)", "Lao (la République démocratique populaire)", "Laos (República Democrática Popular do)"),
	LBN("422", "LB", new String[]{".lb"}, "Lebanon", "Liban (le)", "Líbano"),
	LSO("426", "LS", new String[]{".ls"}, "Lesotho", "Lesotho (le)", "Lesoto"),
	LVA("428", "LV", new String[]{".lv"}, "Latvia", "Lettonie (la)", "Letónia"),
	LBR("430", "LR", new String[]{".lr"}, "Liberia", "Libéria (le)", "Libéria"),
	LBY("434", "LY", new String[]{".ly"}, "Libya", "Libye (la)", "Líbia"),
	LIE("438", "LI", new String[]{".li"}, "Liechtenstein", "Liechtenstein (le)", "Liechtenstein"),
	LTU("440", "LT", new String[]{".lt"}, "Lithuania", "Lituanie (la)", "Lituânia"),
	LUX("442", "LU", new String[]{".lu"}, "Luxembourg", "Luxembourg (le)", "Luxemburgo"),
	MAC("446", "MO", new String[]{".mo"}, "Macao", "Macao", "Macau"),
	MDG("450", "MG", new String[]{".mg"}, "Madagascar", "Madagascar", "Madagáscar"),
	MWI("454", "MW", new String[]{".mw"}, "Malawi", "Malawi (le)", "Malawi"),
	MYS("458", "MY", new String[]{".my"}, "Malaysia", "Malaisie (la)", "Malásia"),
	MDV("462", "MV", new String[]{".mv"}, "Maldives", "Maldives (les)", "Maldivas"),
	MLI("466", "ML", new String[]{".ml"}, "Mali", "Mali (le)", "Mali"),
	MLT("470", "MT", new String[]{".mt"}, "Malta", "Malte", "Malta"),
	MTQ("474", "MQ", new String[]{".mq"}, "Martinique", "Martinique (la)", "Martinica"),
	MRT("478", "MR", new String[]{".mr"}, "Mauritania", "Mauritanie (la)", "Mauritânia"),
	MUS("480", "MU", new String[]{".mu"}, "Mauritius", "Maurice", "Maurícia"),
	MEX("484", "MX", new String[]{".mx"}, "Mexico", "Mexique (le)", "México"),
	MCO("492", "MC", new String[]{".mc"}, "Monaco", "Monaco", "Mônaco"),
	MNG("496", "MN", new String[]{".mn"}, "Mongolia", "Mongolie (la)", "Mongólia"),
	MDA("498", "MD", new String[]{".md"}, "Moldova (the Republic of)", "Moldova (la République de)", "Moldávia (República da)"),
	MNE("499", "ME", new String[]{".me"}, "Montenegro", "Monténégro (le)", "Montenegro"),
	MSR("500", "MS", new String[]{".ms"}, "Montserrat", "Montserrat", "Montserrat"),
	MAR("504", "MA", new String[]{".ma"}, "Morocco", "Maroc (le)", "Marrocos"),
	MOZ("508", "MZ", new String[]{".mz"}, "Mozambique", "Mozambique (le)", "Moçambique"),
	OMN("512", "OM", new String[]{".om"}, "Oman", "Oman", "Omã"),
	NAM("516", "NA", new String[]{".na"}, "Namibia", "Namibie (la)", "Namíbia"),
	NRU("520", "NR", new String[]{".nr"}, "Nauru", "Nauru", "Nauru"),
	NPL("524", "NP", new String[]{".np"}, "Nepal", "Népal (le)", "Nepal"),
	NLD("528", "NL", new String[]{".nl"}, "Netherlands (the)", "Pays-Bas (les)", "Países Baixos"),
	CUW("531", "CW", new String[]{".cw"}, "Curaçao", "Curaçao", "Curaçao"),
	ABW("533", "AW", new String[]{".aw"}, "Aruba", "Aruba", "Aruba"),
	SXM("534", "SX", new String[]{".sx"}, "Sint Maarten (Dutch part)", "Saint-Martin (partie néerlandaise)", "São Martinho (parte holandesa)"),
	BES("535", "BQ", new String[]{".bq", ".nl"}, "Bonaire, Sint Eustatius and Saba", "Bonaire, Saint-Eustache et Saba", "Bonaire, Santo Eustáquio e Saba"),
	NCL("540", "NC", new String[]{".nc"}, "New Caledonia", "Nouvelle-Calédonie (la)", "Nova Caledónia"),
	VUT("548", "VU", new String[]{".vu"}, "Vanuatu", "Vanuatu (le)", "Vanuatu"),
	NZL("554", "NZ", new String[]{".nz"}, "New Zealand", "Nouvelle-Zélande (la)", "Nova Zelândia"),
	NIC("558", "NI", new String[]{".ni"}, "Nicaragua", "Nicaragua (le)", "Nicarágua"),
	NER("562", "NE", new String[]{".ne"}, "Niger (the)", "Niger (le)", "Níger"),
	NGA("566", "NG", new String[]{".ng"}, "Nigeria", "Nigéria (le)", "Nigéria"),
	NIU("570", "NU", new String[]{".nu"}, "Niue", "Niue", "Niue"),
	NFK("574", "NF", new String[]{".nf"}, "Norfolk Island", "Norfolk (l'Île)", "Ilha Norfolk"),
	NOR("578", "NO", new String[]{".no"}, "Norway", "Norvège (la)", "Noruega"),
	MNP("580", "MP", new String[]{".mp"}, "Northern Mariana Islands (the)", "Mariannes du Nord (les Îles)", "Ilhas Marianas do Norte"),
	UMI("581", "UM", new String[]{}, "United States Minor Outlying Islands (the)", "Îles mineures éloignées des États-Unis (les)", "Ilhas Menores Distantes dos Estados Unidos"),
	FSM("583", "FM", new String[]{".fm"}, "Micronesia (Federated States of)", "Micronésie (États fédérés de)", "Micronésia (Estados Federados da)"),
	MHL("584", "MH", new String[]{".mh"}, "Marshall Islands (the)", "Marshall (les Îles)", "Ilhas Marshall"),
	PLW("585", "PW", new String[]{".pw"}, "Palau", "Palaos (les)", "Palau"),
	PAK("586", "PK", new String[]{".pk"}, "Pakistan", "Pakistan (le)", "Paquistão"),
	PAN("591", "PA", new String[]{".pa"}, "Panama", "Panama (le)", "Panamá"),
	PNG("598", "PG", new String[]{".pg"}, "Papua New Guinea", "Papouasie-Nouvelle-Guinée (la)", "Papua-Nova Guiné"),
	PRY("600", "PY", new String[]{".py"}, "Paraguay", "Paraguay (le)", "Paraguai"),
	PER("604", "PE", new String[]{".pe"}, "Peru", "Pérou (le)", "Peru"),
	PHL("608", "PH", new String[]{".ph"}, "Philippines (the)", "Philippines (les)", "Filipinas"),
	PCN("612", "PN", new String[]{".pn"}, "Pitcairn", "Pitcairn", "Ilhas Pitcairn"),
	POL("616", "PL", new String[]{".pl"}, "Poland", "Pologne (la)", "Polônia"),
	PRT("620", "PT", new String[]{".pt"}, "Portugal", "Portugal (le)", "Portugal"),
	GNB("624", "GW", new String[]{".gw"}, "Guinea-Bissau", "Guinée-Bissau (la)", "Guiné-Bissau"),
	TLS("626", "TL", new String[]{".tl"}, "Timor-Leste", "Timor-Leste (le)", "Timor-Leste"),
	PRI("630", "PR", new String[]{".pr"}, "Puerto Rico", "Porto Rico", "Porto Rico"),
	QAT("634", "QA", new String[]{".qa"}, "Qatar", "Qatar (le)", "Qatar"),
	REU("638", "RE", new String[]{".re"}, "Réunion", "Réunion (La)", "Reunião"),
	ROU("642", "RO", new String[]{".ro"}, "Romania", "Roumanie (la)", "Romênia"),
	RUS("643", "RU", new String[]{".ru"}, "Russian Federation (the)", "Russie (la Fédération de)", "Federação Russa"),
	RWA("646", "RW", new String[]{".rw"}, "Rwanda", "Rwanda (le)", "Rwanda"),
	BLM("652", "BL", new String[]{".bl"}, "Saint Barthélemy", "Saint-Barthélemy", "São Bartolomeu"),
	SHN("654", "SH", new String[]{".sh"}, "Saint Helena, Ascension and Tristan da Cunha", "Sainte-Hélène, Ascension et Tristan da Cunha", "Santa Helena, Ascensão e Tristão da Cunha"),
	KNA("659", "KN", new String[]{".kn"}, "Saint Kitts and Nevis", "Saint-Kitts-et-Nevis", "Saint Kitts e Nevis"),
	AIA("660", "AI", new String[]{".ai"}, "Anguilla", "Anguilla", "Anguilla"),
	LCA("662", "LC", new String[]{".lc"}, "Saint Lucia", "Sainte-Lucie", "Santa Lúcia"),
	MAF("663", "MF", new String[]{".mf"}, "Saint Martin (French part)", "Saint-Martin (partie française)", "Saint-Martin (parte francesa)"),
	SPM("666", "PM", new String[]{".pm"}, "Saint Pierre and Miquelon", "Saint-Pierre-et-Miquelon", "Saint Pierre e Miquelon"),
	VCT("670", "VC", new String[]{".vc"}, "Saint Vincent and the Grenadines", "Saint-Vincent-et-les Grenadines", "São Vicente e Granadinas"),
	SMR("674", "SM", new String[]{".sm"}, "San Marino", "Saint-Marin", "San Marino"),
	STP("678", "ST", new String[]{".st"}, "Sao Tome and Principe", "Sao Tomé-et-Principe", "São Tomé e Príncipe"),
	SAU("682", "SA", new String[]{".sa"}, "Saudi Arabia", "Arabie saoudite (l')", "Arábia Saudita"),
	SEN("686", "SN", new String[]{".sn"}, "Senegal", "Sénégal (le)", "Senegal"),
	SRB("688", "RS", new String[]{".rs"}, "Serbia", "Serbie (la)", "Sérvia"),
	SYC("690", "SC", new String[]{".sc"}, "Seychelles", "Seychelles (les)", "Seychelles"),
	SLE("694", "SL", new String[]{".sl"}, "Sierra Leone", "Sierra Leone (la)", "Serra Leoa"),
	SGP("702", "SG", new String[]{".sg"}, "Singapore", "Singapour", "Singapura"),
	SVK("703", "SK", new String[]{".sk"}, "Slovakia", "Slovaquie (la)", "Eslováquia"),
	VNM("704", "VN", new String[]{".vn"}, "Viet Nam", "Viet Nam (le)", "Vietnã"),
	SVN("705", "SI", new String[]{".si"}, "Slovenia", "Slovénie (la)", "Eslovênia"),
	SOM("706", "SO", new String[]{".so"}, "Somalia", "Somalie (la)", "Somália"),
	ZAF("710", "ZA", new String[]{".za"}, "South Africa", "Afrique du Sud (l')", "África do Sul"),
	ZWE("716", "ZW", new String[]{".zw"}, "Zimbabwe", "Zimbabwe (le)", "Zimbabwe"),
	ESP("724", "ES", new String[]{".es"}, "Spain", "Espagne (l')", "Espanha"),
	SSD("728", "SS", new String[]{".ss"}, "South Sudan", "Soudan du Sud (le)", "Sudão do Sul"),
	SDN("729", "SD", new String[]{".sd"}, "Sudan (the)", "Soudan (le)", "Sudão"),
	ESH("732", "EH", new String[]{}, "Western Sahara", "Sahara occidental (le)", "Saara Ocidental"),
	SUR("740", "SR", new String[]{".sr"}, "Suriname", "Suriname (le)", "Suriname"),
	SJM("744", "SJ", new String[]{}, "Svalbard and Jan Mayen", "Svalbard et l'Île Jan Mayen (le)", "Svalbard e Jan Mayen"),
	SWZ("748", "SZ", new String[]{".sz"}, "Eswatini", "Eswatini (l')", "Suazilândia"),
	SWE("752", "SE", new String[]{".se"}, "Sweden", "Suède (la)", "Suécia"),
	CHE("756", "CH", new String[]{".ch"}, "Switzerland", "Suisse (la)", "Suíça"),
	SYR("760", "SY", new String[]{".sy"}, "Syrian Arab Republic (the)", "République arabe syrienne (la)", "República Árabe Síria"),
	TJK("762", "TJ", new String[]{".tj"}, "Tajikistan", "Tadjikistan (le)", "Tadjiquistão"),
	THA("764", "TH", new String[]{".th"}, "Thailand", "Thaïlande (la)", "Tailândia"),
	TGO("768", "TG", new String[]{".tg"}, "Togo", "Togo (le)", "Togo"),
	TKL("772", "TK", new String[]{".tk"}, "Tokelau", "Tokelau (les)", "Tokelau"),
	TON("776", "TO", new String[]{".to"}, "Tonga", "Tonga (les)", "Tonga"),
	TTO("780", "TT", new String[]{".tt"}, "Trinidad and Tobago", "Trinité-et-Tobago (la)", "Trinidad e Tobago"),
	ARE("784", "AE", new String[]{".ae"}, "United Arab Emirates (the)", "Émirats arabes unis (les)", "Emirados Árabes Unidos"),
	TUN("788", "TN", new String[]{".tn"}, "Tunisia", "Tunisie (la)", "Tunísia"),
	TUR("792", "TR", new String[]{".tr"}, "Turkey", "Turquie (la)", "Turquia"),
	TKM("795", "TM", new String[]{".tm"}, "Turkmenistan", "Turkménistan (le)", "Turquemenistão"),
	TCA("796", "TC", new String[]{".tc"}, "Turks and Caicos Islands (the)", "Turks-et-Caïcos (les Îles)", "Ilhas Turks e Caicos"),
	TUV("798", "TV", new String[]{".tv"}, "Tuvalu", "Tuvalu (les)", "Tuvalu"),
	UGA("800", "UG", new String[]{".ug"}, "Uganda", "Ouganda (l')", "Uganda"),
	UKR("804", "UA", new String[]{".ua"}, "Ukraine", "Ukraine (l')", "Ucrânia"),
	MKD("807", "MK", new String[]{".mk"}, "North Macedonia", "Macédoine du Nord (la)", "Macedônia do Norte"),
	EGY("818", "EG", new String[]{".eg"}, "Egypt", "Égypte (l')", "Egito"),
	GBR("826", "GB", new String[]{".gb", ".uk"}, "United Kingdom of Great Britain and Northern Ireland (the)", "Royaume-Uni de Grande-Bretagne et d'Irlande du Nord (le)", "Reino Unido da Grã-Bretanha e Irlanda do Norte"),
	GGY("831", "GG", new String[]{".gg"}, "Guernsey", "Guernesey", "Guernsey"),
	JEY("832", "JE", new String[]{".je"}, "Jersey", "Jersey", "Jersey"),
	IMN("833", "IM", new String[]{".im"}, "Isle of Man", "Île de Man", "Ilha de Man"),
	TZA("834", "TZ", new String[]{".tz"}, "Tanzania (the United Republic of)", "Tanzanie (la République-Unie de)", "Tanzânia (República Unida da)"),
	USA("840", "US", new String[]{".us"}, "United States of America (the)", "États-Unis d'Amérique (les)", "Estados Unidos da América"),
	VIR("850", "VI", new String[]{".vi"}, "Virgin Islands (U.S.)", "Vierges des États-Unis (les Îles)", "Ilhas Virgens Americanas"),
	BFA("854", "BF", new String[]{".bf"}, "Burkina Faso", "Burkina Faso (le)", "Burkina Faso"),
	URY("858", "UY", new String[]{".uy"}, "Uruguay", "Uruguay (l')", "Uruguai"),
	UZB("860", "UZ", new String[]{".uz"}, "Uzbekistan", "Ouzbékistan (l')", "Uzbequistão"),
	VEN("862", "VE", new String[]{".ve"}, "Venezuela (Bolivarian Republic of)", "Venezuela (République bolivarienne du)", "Venezuela (República Bolivariana da)"),
	WLF("876", "WF", new String[]{".wf"}, "Wallis and Futuna", "Wallis-et-Futuna", "Wallis e Futuna"),
	WSM("882", "WS", new String[]{".ws"}, "Samoa", "Samoa (le)", "Samoa"),
	YEM("887", "YE", new String[]{".ye"}, "Yemen", "Yémen (le)", "Iémen"),
	ZMB("894", "ZM", new String[]{".zm"}, "Zambia", "Zambie (la)", "Zâmbia");

	/**
	 * ISO 3166-1 alpha-2 codes are two-letter country codes defined in ISO 3166-1.
	 */
	@NotBlank
	@Size(max = 2, min = 2)
	private final String alpha2Code;

	/**
	 * The term country refers to a political state or nation or its territory.
	 */
	@NotBlank
	@Size(max = 58, min = 4)
	private final String englishName;

	/**
	 * Le terme pays fait référence à un État politique ou à une nation ou à son territoire.
	 */
	@NotBlank
	@Size(max = 56, min = 4)
	private final String frenchName;

	/**
	 * Country code top-level domain (ccTLD).
	 */
	@Size(max = 7, min = 3)
	private final String[] internetCctld;

	/**
	 * ISO 3166-1 numeric codes are three-digit (left padded with zero) country codes defined in ISO 3166-1.
	 */
	@NotBlank
	@Size(max = 3, min = 3)
	private final String numericCode;

	/**
	 * O termo país refere-se a um estado político ou nação ou seu território.
	 */
	@NotBlank
	@Size(max = 46, min = 3)
	private final String portugueseName;

	Entity(@Nonnull final String numericCode, @Nonnull final String alpha2Code, @Nonnull final String[] internetCctld, @Nonnull final String englishName, @Nonnull final String frenchName, @Nonnull final String portugueseName) {
		this.numericCode = numericCode;
		this.alpha2Code = alpha2Code;
		this.internetCctld = internetCctld;
		this.englishName = englishName;
		this.frenchName = frenchName;
		this.portugueseName = portugueseName;
	}

	@Nonnull
	@Override
	public String getIdentity() {
		return name();
	}

	@Nonnull
	public static Entity of(final int numericCode) {
		return of(numericCodeOf(numericCode));
	}

	@Nonnull
	public static Entity of(@Nonnull final String numericCode) {
		return Arrays.stream(values())
			.filter(entity -> entity.numericCode.equals(numericCode))
			.collect(SingletonCollector.toSingleton())
			.orElseThrow(() -> throwException(numericCode));
	}

	@Nonnull
	public static String numericCodeOf(final int numericCode) {
		return String.format("%03d", numericCode);
	}

	@Nonnull
	private static RuntimeException throwException(@Nonnull final String numericCode) {
		@Nonnull final var messageTemplate = "There are no one country identified by numeric code %s.";
		throw new IllegalArgumentException(String.format(messageTemplate, numericCode));
	}
}
