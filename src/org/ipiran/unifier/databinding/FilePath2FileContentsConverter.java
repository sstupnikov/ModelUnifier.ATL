package org.ipiran.unifier.databinding;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.conversion.Converter;
import org.ipiran.unifier.utils.LinkOpener;
import org.ipiran.unifier.utils.NonLegalResourcePathException;

public class FilePath2FileContentsConverter extends Converter {
	private static final Logger log = Logger.getLogger(FilePath2FileContentsConverter.class);
	
	public FilePath2FileContentsConverter() {
		super(String.class, String.class);
	}

	@Override
	public Object convert(Object fromObject) {
		if(fromObject == null){
			return "";
		} else{
			String path = (String)fromObject;
			try {
				return (new LinkOpener(path)).getFileContents();
			} catch (NonLegalResourcePathException e) {
				log.warn(e.getMessage());
				e.printStackTrace();
			}
			return "";
		}
	}

}
