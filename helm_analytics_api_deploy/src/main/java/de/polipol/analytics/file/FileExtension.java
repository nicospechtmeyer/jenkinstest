package de.polipol.analytics.file;

public enum FileExtension {
	AUX, BMP, DOCX, HTML, JPEG, JPG, JSON, LOG, MD, OUT, PDF, PNG, PRETTYJSON, R, RHTML, RMD, RNW, SVG, TEX, TIFF, TXT,
	XML, XLS, XLSX;

	private static final String[] array;

	static {
		array = new String[FileExtension.values().length];
		for (FileExtension value : FileExtension.values())
			array[value.ordinal()] = value.toString();
	}

	public static FileExtension map(final String extension) {
		switch (extension.toLowerCase()) {
		case "aux":
			return AUX;
		case "bmp":
			return BMP;
		case "docx":
			return DOCX;
		case "html":
			return HTML;
		case "jpeg":
			return JPEG;
		case "jpg":
			return JPG;
		case "json":
			return JSON;
		case "log":
			return LOG;
		case "md":
			return MD;
		case "out":
			return OUT;
		case "pdf":
			return PDF;
		case "png":
			return PNG;
		case "prettyjson":
			return PRETTYJSON;
		case "r":
			return R;
		case "rhtml":
			return RHTML;
		case "rmd":
			return RMD;
		case "rnw":
			return RNW;
		case "svg":
			return SVG;
		case "tex":
			return TEX;
		case "tiff":
			return TIFF;
		case "txt":
			return TXT;
		case "xml":
			return XML;
		case "xls":
			return XLS;
		case "xlsx":
			return XLSX;
		default:
			return JSON;
		}
	}

	public static String[] toArray() {
		return array;
	}
}
