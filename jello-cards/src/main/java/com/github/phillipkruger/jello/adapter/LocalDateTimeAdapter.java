package com.github.phillipkruger.jello.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML-Object-XML for LocalDateTime
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    
    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return LocalDateTime.parse(v,DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        return v.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
