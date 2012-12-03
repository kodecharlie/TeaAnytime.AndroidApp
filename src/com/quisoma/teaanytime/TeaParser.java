package com.quisoma.teaanytime;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class TeaParser
{
    // XML tags.
	static final String ROOT_ELEMENT = "TeaAnytime";
	static final String TEA = "Tea";
	static final String NAME = "Name";
    static final String GROUP = "Group";
    static final String IMAGE_FILE_NAME = "ImageFileName";
	static final String DESCRIPTION = "Description";

	// Tea specifications, in XML.
	static final String TEA_ANYTIME_XML = "TeaAnytime.xml";

	private AssetManager assetManager;

    protected TeaParser(AssetManager assetManager) {
    	this.assetManager = assetManager;
    }

    protected InputStream getInputStream() {
        try {
            return assetManager.open(TEA_ANYTIME_XML);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Tea> parse() {
        final Tea curTea = new Tea();
        RootElement root = new RootElement(ROOT_ELEMENT);
        final List<Tea> teas = new ArrayList<Tea>();

        Element tea = root.getChild(TEA);
        tea.setEndElementListener(new EndElementListener(){
            public void end() {
                teas.add(curTea.copy());
            }
        });
        tea.getChild(NAME).setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
                curTea.setName(body);
            }
        });
        tea.getChild(GROUP).setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
                curTea.setGroup(body);
            }
        });
        tea.getChild(IMAGE_FILE_NAME).setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
                curTea.setImageFileName(body);
            }
        });
        tea.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
                curTea.setDescription(body);
            }
        });

        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return teas;
    }
}
