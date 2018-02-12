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
            //NodeCollection<Paragraph> paragraphCollection = doc.getFirstSection().getBody().getChildNodes(NodeType.PARAGRAPH, true);
            NodeCollection nodeCollection = doc.getFirstSection().getBody().getChildNodes();
            Paragraph startPara = null;
            Paragraph endPara = null;
            for(Node node : (Iterable<Node>) nodeCollection){
                if(node.getNodeType() == NodeType.PARAGRAPH) {
                    Paragraph paragraph = (Paragraph) node;
                    if(ProcessData.isValid(paragraph.getRuns()) && ProcessData.isValid(paragraph.getRuns().get(0))
                            && ProcessData.isValid(paragraph.getRuns().get(0).getText())){
                        System.out.println("Paragraph text is now " + paragraph.getRuns().get(0).getText()
                                + " and paragraph index is now " + paragraph.getAncestor(NodeType.BODY).indexOf(paragraph));
                        if(paragraph.getRuns().get(0).getText().contains("==START==")){
                            Paragraph newParagraph = new Paragraph(doc);
                            //doc.getFirstSection().getBody().appendChild(newParagraph);
                            Node nodeParent  = node.getParentNode();
                            System.out.println("Node parent is now " + nodeParent.getNodeType() + " and text is " + nodeParent.toString());
                            Run run = new Run(doc);
                            run.setText("BODY_CONTENT");
                            newParagraph.appendChild(run);
                            doc.getFirstSection().getBody().insertBefore(newParagraph, node);
                            startPara = newParagraph;
                        } else if(paragraph.getRuns().get(0).getText().contains("==END==")){
                            endPara = (Paragraph) node;
                        }
                    }
                } else {
                    System.out.println("Node is not of type paragraph. The NodeType is " + node.getNodeType() + " and text is " + node.toString() );
                }
            }
            System.out.println("Now extracting paragraph content from ==start== to ==end== ");
            removeMarkedBodyBetweenParagraph(doc, startPara, endPara);
            /*
            NodeCollection newNodeCollection = doc.getFirstSection().getBody().getChildNodes();
            for(Node node : (Iterable<Node>) newNodeCollection){
                if(node.getNodeType() == NodeType.PARAGRAPH) {
                    Paragraph paragraph = (Paragraph) node;
                    if(ProcessData.isValid(paragraph.getRuns()) && ProcessData.isValid(paragraph.getRuns().get(0))
                            && ProcessData.isValid(paragraph.getRuns().get(0).getText())){
                        System.out.println("Paragraph text is now " + paragraph.getRuns().get(0).getText()
                                + " and paragraph index is now " + paragraph.getAncestor(NodeType.BODY).indexOf(paragraph));
                        if(paragraph.getRuns().get(0).getText().contains("==START==")){
                            Paragraph newParagraph = new Paragraph(doc);
                            Run run = new Run(doc);
                            run.setText("BODY_CONTENT");
                            newParagraph.appendChild(run);
                            doc.insertAfter(newParagraph, node);
                        }
                    }
                } else {
                    System.out.println("Node is not of type paragraph. The NodeType is " + node.getNodeType() + " and text is " + node.toString() );
                }
            } */
            //ArrayList extractedNodes = null;
            //Document dstDoc = generateDocument(doc, extractedNodes);
            //dstDoc.save(dataDir + "output.docx");
            doc.save(dataDir + "bmaninder_output.docx");
            System.out.println("Now exiting from extractContentBetweenServices");
        } catch(Exception ex){
            System.out.println("Excption caught is now " + ex);
        }
    }

    public static void removeMarkedBodyBetweenParagraph(Document doc, Paragraph start, Paragraph end) throws Exception {
        if (ProcessData.isValid(start)  && ProcessData.isValid(end) ) {
            DocumentBuilder builder = new DocumentBuilder(doc);
            builder.moveTo(start);

            BookmarkStart bmStart = builder.startBookmark("bm");
            BookmarkEnd bmEnd = builder.endBookmark("bm");

            end.appendChild(bmEnd);

            bmStart.getBookmark().setText("");
            bmStart.getBookmark().remove();
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
