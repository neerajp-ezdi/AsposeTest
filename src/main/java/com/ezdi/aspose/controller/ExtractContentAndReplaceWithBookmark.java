package com.ezdi.aspose.controller;

import com.aspose.words.*;
import com.ezdi.aspose.util.Util;
import com.ezdi.ezUtility.ProcessData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ExtractContentAndReplaceWithBookmark {

        @GetMapping("extractAndPlaceBookMark")
    public void extractContentBetweenServices(){
        try{
            System.out.println("Now inside extractContentBetweenServices");
            String dataDir = Util.getDataDir();
            Document doc = new Document(dataDir + "bmaninder.docx");
            NodeCollection<Paragraph> paragraphCollection = doc.getFirstSection().getBody().getChildNodes(NodeType.PARAGRAPH, true);
            for(Paragraph paragraph : (Iterable<Paragraph>) paragraphCollection){
                if(ProcessData.isValid(paragraph.getRuns())){
                    if(ProcessData.isValid(paragraph.getRuns().get(0))) {
                        if(ProcessData.isValid(paragraph.getRuns().get(0).getText()))
                        System.out.println("Paragraph text is now " + paragraph.getRuns().get(0).getText()
                                + " and paragraph index is now " + paragraph.getAncestor(NodeType.BODY).indexOf(paragraph));

                    }
                }
            }
            //ArrayList extractedNodes = null;
            //Document dstDoc = generateDocument(doc, extractedNodes);
            //dstDoc.save(dataDir + "output.docx");
            System.out.println("Now exiting from extractContentBetweenServices");
        } catch(Exception ex){
            System.out.println("Excption caught is now " + ex);
        }
    }

    public static Document generateDocument(Document srcDoc, ArrayList nodes) throws Exception {

        // Create a blank document.
        Document dstDoc = new Document();
        // Remove the first paragraph from the empty document.
        dstDoc.getFirstSection().getBody().removeAllChildren();

        // Import each node from the list into the new document. Keep the original formatting of the node.
        NodeImporter importer = new NodeImporter(srcDoc, dstDoc, ImportFormatMode.KEEP_SOURCE_FORMATTING);

        for (Node node : (Iterable<Node>) nodes) {
            Node importNode = importer.importNode(node, true);
            dstDoc.getFirstSection().getBody().appendChild(importNode);
        }

        // Return the generated document.
        return dstDoc;
    }
}
