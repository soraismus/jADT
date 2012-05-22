/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package pogofish.jadt;

import java.io.*;

import pogofish.jadt.ast.Doc;
import pogofish.jadt.emitter.*;
import pogofish.jadt.parser.Parser;
import pogofish.jadt.parser.StandardParser;
import pogofish.jadt.printer.Printer;


public class JADT {
    private final Parser parser;
    private final DocEmitter emitter;
    
    public JADT(Parser parser, DocEmitter emitter) {
        super();
        this.parser = parser;
        this.emitter = emitter;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: java sfdc.adt.ADT [source file] [output directory]");
            System.exit(1);
        }
        final String srcFileName = args[0];
        final String destDirName = args[1];
        
        final JADT adt = new JADT(new StandardParser(), new StandardDocEmitter(new FileTargetFactory(destDirName), new StandardDataTypeEmitter(new StandardClassBodyEmitter(), new StandardConstructorEmitter(new StandardClassBodyEmitter())), new Printer()));        
        adt.parseAndEmit(srcFileName);
    }
    
    public void parseAndEmit(String srcFileName) throws IOException {
        final File srcFile = new File(srcFileName);
        final Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "UTF-8"));
        try {
            parseAndEmit(srcFile.getAbsolutePath(), reader);            
        } finally {
            reader.close();
        }
    }
    
    public void parseAndEmit(String srcInfo, Reader src) throws IOException {
        final Doc doc = parser.parse(srcInfo, src);
        emitter.emit(doc);                
    }
}
