/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.utilities;

import java.io.IOException;

public interface Observable{

    void subscribe(Observer o);

    void unsubcribe(Observer o);

    void notifyObservers(String s) throws IOException;

}
