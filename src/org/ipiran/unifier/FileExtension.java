package org.ipiran.unifier;

public enum FileExtension {
	ALL { public String toString() { return "*"; } },
	ECORE { public String toString() { return "ecore"; } }, 
	ATL { public String toString() { return "atl"; } }, 
	TXT { public String toString() { return "txt"; } },
	PDF { public String toString() { return "pdf"; } },
	CS { public String toString() { return "cs"; } },
	REF { public String toString() { return "ref"; } },
	MCH { public String toString() { return "mch"; } },	
	MRC { public String toString() { return "mrc"; } },
	ERC { public String toString() { return "erc"; } },
	SRC { public String toString() { return "src"; } },
	UNI { public String toString() { return "uni"; } },
	SIM { public String toString() { return "sim"; } }
}
