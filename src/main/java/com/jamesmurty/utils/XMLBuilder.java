/*
 * Copyright 2008-2014 James Murty (www.jamesmurty.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * This code is available from the GitHub code repository at:
 * https://github.com/jmurty/java-xmlbuilder
 */
package com.jamesmurty.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML Builder is a utility that creates simple XML documents using relatively
 * sparse Java code. It is intended to allow for quick and painless creation of
 * XML documents where you might otherwise be tempted to use concatenated
 * strings, rather than face the tedium and verbosity of coding with
 * JAXP (http://jaxp.dev.java.net/).
 * <p>
 * Internally, XML Builder uses JAXP to build a standard W3C
 * {@link org.w3c.dom.Document} model (DOM) that you can easily export as a
 * string, or access and manipulate further if you have special requirements.
 * </p>
 * <p>
 * The XMLBuilder class serves as a wrapper of {@link org.w3c.dom.Element} nodes,
 * and provides a number of utility methods that make it simple to
 * manipulate the underlying element and the document to which it belongs.
 * In essence, this class performs dual roles: it represents a specific XML
 * node, and also allows manipulation of the entire underlying XML document.
 * The platform's default {@link DocumentBuilderFactory} and
 * {@link DocumentBuilder} classes are used to build the document.
 * </p>
 *
 * @author James Murty
 */
public class XMLBuilder extends BaseXMLBuilder {

    /**
     * Construct a new builder object that wraps the given XML document.
     * This constructor is for internal use only.
     *
     * @param xmlDocument
     * an XML document that the builder will manage and manipulate.
     */
    protected XMLBuilder(Document xmlDocument) {
        super(xmlDocument);
    }

    /**
     * Construct a new builder object that wraps the given XML document and node.
     * This constructor is for internal use only.
     *
     * @param myNode
     * the XML node that this builder node will wrap. This node may
     * be part of the XML document, or it may be a new element that is to be
     * added to the document.
     * @param parentNode
     * If not null, the given myElement will be appended as child node of the
     * parentNode node.
     */
    protected XMLBuilder(Node myNode, Node parentNode) {
        super(myNode, parentNode);
    }

    /**
     * Construct a builder for new XML document with a default namespace.
     * The document will be created with the given root element, and the builder
     * returned by this method will serve as the starting-point for any further
     * document additions.
     *
     * @param name
     * the name of the document's root element.
     * @param namespaceURI
     * default namespace URI for document, ignored if null or empty.
     * @return
     * a builder node that can be used to add more nodes to the XML document.
     *
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     */
    public static XMLBuilder create(String name, String namespaceURI)
        throws ParserConfigurationException, FactoryConfigurationError
    {
        return new XMLBuilder(createDocumentImpl(name, namespaceURI));
    }

    /**
     * Construct a builder for new XML document. The document will be created
     * with the given root element, and the builder returned by this method
     * will serve as the starting-point for any further document additions.
     *
     * @param name
     * the name of the document's root element.
     * @return
     * a builder node that can be used to add more nodes to the XML document.
     *
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     */
    public static XMLBuilder create(String name)
        throws ParserConfigurationException, FactoryConfigurationError
    {
        return create(name, null);
    }

    /**
     * Construct a builder from an existing XML document. The provided XML
     * document will be parsed and an XMLBuilder object referencing the
     * document's root element will be returned.
     *
     * @param inputSource
     * an XML document input source that will be parsed into a DOM.
     * @return
     * a builder node that can be used to add more nodes to the XML document.
     * @throws ParserConfigurationException
     *
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static XMLBuilder parse(InputSource inputSource)
        throws ParserConfigurationException, SAXException, IOException
    {
        return new XMLBuilder(parseDocumentImpl(inputSource));
    }

    /**
     * Construct a builder from an existing XML document string.
     * The provided XML document will be parsed and an XMLBuilder
     * object referencing the document's root element will be returned.
     *
     * @param xmlString
     * an XML document string that will be parsed into a DOM.
     * @return
     * a builder node that can be used to add more nodes to the XML document.
     *
     * @throws ParserConfigurationException
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static XMLBuilder parse(String xmlString)
        throws ParserConfigurationException, SAXException, IOException
    {
        return XMLBuilder.parse(new InputSource(new StringReader(xmlString)));
    }

    /**
     * Construct a builder from an existing XML document file.
     * The provided XML document will be parsed and an XMLBuilder
     * object referencing the document's root element will be returned.
     *
     * @param xmlFile
     * an XML document file that will be parsed into a DOM.
     * @return
     * a builder node that can be used to add more nodes to the XML document.
     *
     * @throws ParserConfigurationException
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static XMLBuilder parse(File xmlFile)
        throws ParserConfigurationException, SAXException, IOException
    {
        return XMLBuilder.parse(new InputSource(new FileReader(xmlFile)));
    }

    /**
     * Find and delete from the underlying Document any text nodes that
     * contain nothing but whitespace, such as newlines and tab or space
     * characters used to indent or pretty-print an XML document.
     *
     * Uses approach I documented on StackOverflow:
     * http://stackoverflow.com/a/979606/4970
     *
     * @return
     * a builder node at the same location as before the operation.
     * @throws XPathExpressionException
     */
    public XMLBuilder stripWhitespaceOnlyTextNodes()
        throws XPathExpressionException
    {
        super.stripWhitespaceOnlyTextNodesImpl();
        return this;
    }

    /**
     * Imports another XMLBuilder document into this document at the
     * current position. The entire document provided is imported.
     *
     * @param builder
     * the XMLBuilder document to be imported.
     *
     * @return
     * a builder node at the same location as before the import, but
     * now containing the entire document tree provided.
     */
    public XMLBuilder importXMLBuilder(XMLBuilder builder) {
        super.importXMLBuilderImpl(builder);
        return this;
    }

    /**
     * @return
     * the builder node representing the root element of the XML document.
     * In other words, the same builder node returned by the initial
     * {@link #create(String)} or {@link #parse(InputSource)} method.
     */
    public XMLBuilder root() {
        return new XMLBuilder(getDocument());
    }

    /**
     * Find the first element in the builder's DOM matching the given
     * XPath expression, where the expression may include namespaces if
     * a {@link NamespaceContext} is provided.
     *
     * @param xpath
     * An XPath expression that *must* resolve to an existing Element within
     * the document object model.
     * @param nsContext
     * a mapping of prefixes to namespace URIs that allows the XPath expression
     * to use namespaces.
     *
     * @return
     * a builder node representing the first Element that matches the
     * XPath expression.
     *
     * @throws XPathExpressionException
     * If the XPath is invalid, or if does not resolve to at least one
     * {@link Node#ELEMENT_NODE}.
     */
    public XMLBuilder xpathFind(String xpath, NamespaceContext nsContext)
        throws XPathExpressionException
    {
        Node foundNode = super.xpathFindImpl(xpath, nsContext);
        return new XMLBuilder(foundNode, null);
    }

    /**
     * Find the first element in the builder's DOM matching the given
     * XPath expression.
     *
     * @param xpath
     * An XPath expression that *must* resolve to an existing Element within
     * the document object model.
     *
     * @return
     * a builder node representing the first Element that matches the
     * XPath expression.
     *
     * @throws XPathExpressionException
     * If the XPath is invalid, or if does not resolve to at least one
     * {@link Node#ELEMENT_NODE}.
     */
    public XMLBuilder xpathFind(String xpath) throws XPathExpressionException {
        return xpathFind(xpath, null);
    }

    /**
     * Add a named XML element to the document as a child of this builder node,
     * and return the builder node representing the new child.
     *
     * When adding an element to a namespaced document, the new node will be
     * assigned a namespace matching it's qualified name prefix (if any) or
     * the document's default namespace. NOTE: If the element has a prefix that
     * does not match any known namespaces, the element will be created
     * without any namespace.
     *
     * @param name
     * the name of the XML element.
     *
     * @return
     * a builder node representing the new child.
     *
     * @throws IllegalStateException
     * if you attempt to add a child element to an XML node that already
     * contains a text node value.
     */
    public XMLBuilder element(String name) {
        String namespaceURI = super.lookupNamespaceURIImpl(name);
        return element(name, namespaceURI);
    }

    /**
     * Synonym for {@link #element(String)}.
     *
     * @param name
     * the name of the XML element.
     *
     * @return
     * a builder node representing the new child.
     *
     * @throws IllegalStateException
     * if you attempt to add a child element to an XML node that already
     * contains a text node value.
     */
    public XMLBuilder elem(String name) {
        return element(name);
    }

    /**
     * Synonym for {@link #element(String)}.
     *
     * @param name
     * the name of the XML element.
     *
     * @return
     * a builder node representing the new child.
     *
     * @throws IllegalStateException
     * if you attempt to add a child element to an XML node that already
     * contains a text node value.
     */
    public XMLBuilder e(String name) {
        return element(name);
    }

    /**
     * Add a named and namespaced XML element to the document as a child of
     * this builder node, and return the builder node representing the new child.
     *
     * @param name
     * the name of the XML element.
     * @param namespaceURI
     * a namespace URI
     *
     * @return
     * a builder node representing the new child.
     *
     * @throws IllegalStateException
     * if you attempt to add a child element to an XML node that already
     * contains a text node value.
     */
    public XMLBuilder element(String name, String namespaceURI) {
        Element elem = super.elementImpl(name, namespaceURI);
        return new XMLBuilder(elem, this.getElement());
    }

    /**
     * Add a named XML element to the document as a sibling element
     * that precedes the position of this builder node, and return the builder node
     * representing the new child.
     *
     * When adding an element to a namespaced document, the new node will be
     * assigned a namespace matching it's qualified name prefix (if any) or
     * the document's default namespace. NOTE: If the element has a prefix that
     * does not match any known namespaces, the element will be created
     * without any namespace.
     *
     * @param name
     * the name of the XML element.
     *
     * @return
     * a builder node representing the new child.
     *
     * @throws IllegalStateException
     * if you attempt to add a sibling element to a node where there are already
     * one or more siblings that are text nodes.
     */
    public XMLBuilder elementBefore(String name) {
        Element newElement = super.elementBeforeImpl(name);
        return new XMLBuilder(newElement, null);
    }

    /**
     * Add a named and namespaced XML element to the document as a sibling element
     * that precedes the position of this builder node, and return the builder node
     * representing the new child.
     *
     * @param name
     * the name of the XML element.
     * @param namespaceURI
     * a namespace URI
     *
     * @return
     * a builder node representing the new child.
     *
     * @throws IllegalStateException
     * if you attempt to add a sibling element to a node where there are already
     * one or more siblings that are text nodes.
     */
    public XMLBuilder elementBefore(String name, String namespaceURI) {
        Element newElement = super.elementBeforeImpl(name, namespaceURI);
        return new XMLBuilder(newElement, null);
    }

    /**
     * Add a named attribute value to the element represented by this builder
     * node, and return the node representing the element to which the
     * attribute was added (<strong>not</strong> the new attribute node).
     *
     * @param name
     * the attribute's name.
     * @param value
     * the attribute's value.
     *
     * @return
     * the builder node representing the element to which the attribute was
     * added.
     */
    public XMLBuilder attribute(String name, String value) {
        super.attributeImpl(name, value);
        return this;
    }

    /**
     * Synonym for {@link #attribute(String, String)}.
     *
     * @param name
     * the attribute's name.
     * @param value
     * the attribute's value.
     *
     * @return
     * the builder node representing the element to which the attribute was
     * added.
     */
    public XMLBuilder attr(String name, String value) {
        return attribute(name, value);
    }

    /**
     * Synonym for {@link #attribute(String, String)}.
     *
     * @param name
     * the attribute's name.
     * @param value
     * the attribute's value.
     *
     * @return
     * the builder node representing the element to which the attribute was
     * added.
     */
    public XMLBuilder a(String name, String value) {
        return attribute(name, value);
    }


    /**
     * Add or replace the text value of an element represented by this builder
     * node, and return the node representing the element to which the text
     * was added (<strong>not</strong> the new text node).
     *
     * @param value
     * the text value to set or add to the element.
     * @param replaceText
     * if True any existing text content of the node is replaced with the
     * given text value, if the given value is appended to any existing text.
     *
     * @return
     * the builder node representing the element to which the text was added.
     */
    public XMLBuilder text(String value, boolean replaceText) {
        super.textImpl(value, replaceText);
        return this;
    }

    /**
     * Add a text value to the element represented by this builder node, and
     * return the node representing the element to which the text
     * was added (<strong>not</strong> the new text node).
     *
     * @param value
     * the text value to add to the element.
     *
     * @return
     * the builder node representing the element to which the text was added.
     */
    public XMLBuilder text(String value) {
        return this.text(value, false);
    }

    /**
     * Synonym for {@link #text(String)}.
     *
     * @param value
     * the text value to add to the element.
     *
     * @return
     * the builder node representing the element to which the text was added.
     */
    public XMLBuilder t(String value) {
        return text(value);
    }

    /**
     * Add a CDATA node with String content to the element represented by this
     * builder node, and return the node representing the element to which the
     * data was added (<strong>not</strong> the new CDATA node).
     *
     * @param data
     * the String value that will be added to a CDATA element.
     *
     * @return
     * the builder node representing the element to which the data was added.
     */
    public XMLBuilder cdata(String data) {
        super.cdataImpl(data);
        return this;
    }

    /**
     * Synonym for {@link #cdata(String)}.
     *
     * @param data
     * the String value that will be added to a CDATA element.
     *
     * @return
     * the builder node representing the element to which the data was added.
     */
    public XMLBuilder data(String data) {
        return cdata(data);
    }

    /**
     * Synonym for {@link #cdata(String)}.
     *
     * @param data
     * the String value that will be added to a CDATA element.
     *
     * @return
     * the builder node representing the element to which the data was added.
     */
    public XMLBuilder d(String data) {
        return cdata(data);
    }

    /**
     * Add a CDATA node with Base64-encoded byte data content to the element represented
     * by this builder node, and return the node representing the element to which the
     * data was added (<strong>not</strong> the new CDATA node).
     *
     * @param data
     * the data value that will be Base64-encoded and added to a CDATA element.
     *
     * @return
     * the builder node representing the element to which the data was added.
     */
    public XMLBuilder cdata(byte[] data) {
        super.cdataImpl(data);
        return this;
    }

    /**
     * Synonym for {@link #cdata(byte[])}.
     *
     * @param data
     * the data value that will be Base64-encoded and added to a CDATA element.
     *
     * @return
     * the builder node representing the element to which the data was added.
     */
    public XMLBuilder data(byte[] data) {
        return cdata(data);
    }

    /**
     * Synonym for {@link #cdata(byte[])}.
     *
     * @param data
     * the data value that will be Base64-encoded and added to a CDATA element.
     *
     * @return
     * the builder node representing the element to which the data was added.
     */
    public XMLBuilder d(byte[] data) {
        return cdata(data);
    }

    /**
     * Add a comment to the element represented by this builder node, and
     * return the node representing the element to which the comment
     * was added (<strong>not</strong> the new comment node).
     *
     * @param comment
     * the comment to add to the element.
     *
     * @return
     * the builder node representing the element to which the comment was added.
     */
    public XMLBuilder comment(String comment) {
        super.commentImpl(comment);
        return this;
    }

    /**
     * Synonym for {@link #comment(String)}.
     *
     * @param comment
     * the comment to add to the element.
     *
     * @return
     * the builder node representing the element to which the comment was added.
     */
    public XMLBuilder cmnt(String comment) {
        return comment(comment);
    }

    /**
     * Synonym for {@link #comment(String)}.
     *
     * @param comment
     * the comment to add to the element.
     *
     * @return
     * the builder node representing the element to which the comment was added.
     */
    public XMLBuilder c(String comment) {
        return comment(comment);
    }

    /**
     * Add an instruction to the element represented by this builder node, and
     * return the node representing the element to which the instruction
     * was added (<strong>not</strong> the new instruction node).
     *
     * @param target
     * the target value for the instruction.
     * @param data
     * the data value for the instruction
     *
     * @return
     * the builder node representing the element to which the instruction was
     * added.
     */
    public XMLBuilder instruction(String target, String data) {
        super.instructionImpl(target, data);
        return this;
    }

    /**
     * Synonym for {@link #instruction(String, String)}.
     *
     * @param target
     * the target value for the instruction.
     * @param data
     * the data value for the instruction
     *
     * @return
     * the builder node representing the element to which the instruction was
     * added.
     */
    public XMLBuilder inst(String target, String data) {
        return instruction(target, data);
    }

    /**
     * Synonym for {@link #instruction(String, String)}.
     *
     * @param target
     * the target value for the instruction.
     * @param data
     * the data value for the instruction
     *
     * @return
     * the builder node representing the element to which the instruction was
     * added.
     */
    public XMLBuilder i(String target, String data) {
        return instruction(target, data);
    }

    /**
     * Insert an instruction before the element represented by this builder node,
     * and return the node representing that same element
     * (<strong>not</strong> the new instruction node).
     *
     * @param target
     * the target value for the instruction.
     * @param data
     * the data value for the instruction
     *
     * @return
     * the builder node representing the element before which the instruction was inserted.
     */
    public XMLBuilder insertInstruction(String target, String data) {
        super.insertInstructionImpl(target, data);
        return this;
    }

    /**
     * Add a reference to the element represented by this builder node, and
     * return the node representing the element to which the reference
     * was added (<strong>not</strong> the new reference node).
     *
     * @param name
     * the name value for the reference.
     *
     * @return
     * the builder node representing the element to which the reference was
     * added.
     */
    public XMLBuilder reference(String name) {
        super.referenceImpl(name);
        return this;
    }

    /**
     * Synonym for {@link #reference(String)}.
     *
     * @param name
     * the name value for the reference.
     *
     * @return
     * the builder node representing the element to which the reference was
     * added.
     */
    public XMLBuilder ref(String name) {
        return reference(name);
    }

    /**
     * Synonym for {@link #reference(String)}.
     *
     * @param name
     * the name value for the reference.
     *
     * @return
     * the builder node representing the element to which the reference was
     * added.
     */
    public XMLBuilder r(String name) {
        return reference(name);
    }

    /**
     * Add an XML namespace attribute to this builder's element node.
     *
     * @param prefix
     * a prefix for the namespace URI within the document, may be null
     * or empty in which case a default "xmlns" attribute is created.
     * @param namespaceURI
     * a namespace uri
     *
     * @return
     * the builder node representing the element to which the attribute was added.
     */
    public XMLBuilder namespace(String prefix, String namespaceURI) {
        super.namespaceImpl(prefix, namespaceURI);
        return this;
    }

    /**
     * Synonym for {@link #namespace(String, String)}.
     *
     * @param prefix
     * a prefix for the namespace URI within the document, may be null
     * or empty in which case a default xmlns attribute is created.
     * @param namespaceURI
     * a namespace uri
     *
     * @return
     * the builder node representing the element to which the attribute was added.
     */
    public XMLBuilder ns(String prefix, String namespaceURI) {
        return attribute(prefix, namespaceURI);
    }

    /**
     * Add an XML namespace attribute to this builder's element node
     * without a prefix.
     *
     * @param namespaceURI
     * a namespace uri
     *
     * @return
     * the builder node representing the element to which the attribute was added.
     */
    public XMLBuilder namespace(String namespaceURI) {
        this.namespace(null, namespaceURI);
        return this;
    }

    /**
     * Synonym for {@link #namespace(String)}.
     *
     * @param namespaceURI
     * a namespace uri
     *
     * @return
     * the builder node representing the element to which the attribute was added.
     */
    public XMLBuilder ns(String namespaceURI) {
        return namespace(namespaceURI);
    }

    /**
     * Return the builder node representing the n<em>th</em> ancestor element
     * of this node, or the root node if n exceeds the document's depth.
     *
     * @param steps
     * the number of parent elements to step over while navigating up the chain
     * of node ancestors. A steps value of 1 will find a node's parent, 2 will
     * find its grandparent etc.
     *
     * @return
     * the n<em>th</em> ancestor of this node, or the root node if this is
     * reached before the n<em>th</em> parent is found.
     */
    public XMLBuilder up(int steps) {
        Node currNode = super.upImpl(steps);
        if (currNode instanceof Document) {
            return new XMLBuilder((Document) currNode);
        } else {
            return new XMLBuilder(currNode, null);
        }
    }

    /**
     * Return the builder node representing the parent of the current node.
     *
     * @return
     * the parent of this node, or the root node if this method is called on the
     * root node.
     */
    public XMLBuilder up() {
        return up(1);
    }

    /**
     * BEWARE: The builder returned by this method represents a Document node, not
     * an Element node as is usually the case, so attempts to use the attribute or
     * namespace methods on this builder will likely fail.
     *
     * @return
     * the builder node representing the root XML document.
     */
    public XMLBuilder document() {
        return new XMLBuilder(getDocument(), null);
    }

}
