package com.whisppa.droidfluxlib.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 5/5/2015.
 */
public class CollectionUtil {

    public static <T> List<T> intersection(List<T> arrayOne, List<T> arrayTwo)
    {
        List<T> _intersect = new ArrayList<T>();
        for(int i=0; i < arrayOne.size(); i++)
        {
            for(int j=0; j < arrayTwo.size(); j++)
            {
                if(arrayOne.get(i) == arrayTwo.get(j))
                {
                    _intersect.add(arrayOne.get(i));
                }
            }
        }

        return _intersect;
    }

    public static String implode(Iterator it) {
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()) {
            sb.append(it.next());
            sb.append(", ");
        }

        return sb.toString().replaceAll(", +$", "");
    }
}
