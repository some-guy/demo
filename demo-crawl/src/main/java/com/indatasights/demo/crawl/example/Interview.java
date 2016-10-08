package com.indatasights.demo.crawl.example;

import java.util.LinkedList;
import java.util.List;

public class Interview {
	List<Integer> list = new LinkedList<>();
	private static class Node<T> {
		public Node<T> left;
		public Node<T> right;
		public T value;

		public Node(T value) {
			this.value = value;
		}

		public Node<T> getLeft() {
			return this.left;
		}

		public void setLeft(Node<T> left) {
			this.left = left;
		}

		public Node<T> getRight() {
			return this.right;
		}

		public void setRight(Node<T> right) {
			this.right = right;
		}
		public T getValue() {
			return this.value;
		}
	}

	public void addToListInOrder(Node<?> n) {
		if (n != null) {
			addToListInOrder(n.getLeft());
			list.add((Integer)n.getValue());
			addToListInOrder(n.getRight());
		}
	}

	public static void main(final String[] args) {
		Interview i = new Interview();
		Node<Integer> one = new Node<>(1);
		Node<Integer> two = new Node<>(2);
		Node<Integer> three = new Node<>(3);
		Node<Integer> four = new Node<>(4);
		Node<Integer> five = new Node<>(5);
		Node<Integer> six = new Node<>(6);
		Node<Integer> seven = new Node<>(7);

		four.setLeft(two);
		four.setRight(six);

		two.setLeft(one);
		two.setRight(three);

		six.setLeft(five);
		six.setRight(seven);

		i.addToListInOrder(four);
		System.out.println(i.list);

	}
}
