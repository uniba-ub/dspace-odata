package service;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.commons.core.edm.primitivetype.EdmString;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.Literal;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.olingo.server.api.uri.queryoption.expression.MethodKind;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

public class FilterExpressionVisitor implements ExpressionVisitor<Object> {
	private final Entity currentEntity;

	public FilterExpressionVisitor(Entity currentEntity) {
		this.currentEntity = currentEntity;
	}

	public Object visitLiteral(Literal literal) throws ODataApplicationException {

		String literalAsString = literal.getText();
		if (literal.getType() instanceof EdmString) {
			String stringLiteral = "";
			if (literal.getText().length() > 2) {
				stringLiteral = literalAsString.substring(1, literalAsString.length() - 1);
			}

			return stringLiteral;
		} else {
			try {
				return Integer.parseInt(literalAsString);
			} catch (NumberFormatException e) {
				throw new ODataApplicationException("Only Edm.Int32 and Edm.String literals are implemented",
						HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
			}
		}
	}

	public Object visitUnaryOperator(UnaryOperatorKind operator, Object operand)
			throws ODataApplicationException {

		if (operator == UnaryOperatorKind.NOT && operand instanceof Boolean) {
			return !(Boolean) operand;
		} else if (operator == UnaryOperatorKind.MINUS && operand instanceof Integer) {
			return -(Integer) operand;
		}

		throw new ODataApplicationException("Invalid type for unary operator",
				HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
	}

	public Object visitBinaryOperator(BinaryOperatorKind operator, Object left, Object right)
			throws ODataApplicationException {
		if (left == null) {
			return false;
		}
		if (operator == BinaryOperatorKind.ADD || operator == BinaryOperatorKind.MOD
				|| operator == BinaryOperatorKind.MUL || operator == BinaryOperatorKind.DIV
				|| operator == BinaryOperatorKind.SUB) {
			return evaluateArithmeticOperation(operator, left, right);
		} else if (operator == BinaryOperatorKind.EQ || operator == BinaryOperatorKind.NE
				|| operator == BinaryOperatorKind.GE || operator == BinaryOperatorKind.GT
				|| operator == BinaryOperatorKind.LE || operator == BinaryOperatorKind.LT) {
			return evaluateComparisonOperation(operator, left, right);
		} else if (operator == BinaryOperatorKind.AND || operator == BinaryOperatorKind.OR) {
			return evaluateBooleanOperation(operator, left, right);
		} else {
			throw new ODataApplicationException("Binary operation " + operator.name() + " is not implemented",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

	private Object evaluateBooleanOperation(BinaryOperatorKind operator, Object left, Object right)
			throws ODataApplicationException {

		if (left instanceof Boolean && right instanceof Boolean) {
			Boolean valueLeft = (Boolean) left;
			Boolean valueRight = (Boolean) right;

			if (operator == BinaryOperatorKind.AND) {
				return valueLeft && valueRight;
			} else {
				return valueLeft || valueRight;
			}
		} else {
			throw new ODataApplicationException("Boolean operations needs two numeric operands",
					HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
		}
	}

	private Object evaluateComparisonOperation(BinaryOperatorKind operator, Object left, Object right)
			throws ODataApplicationException {
		if (left.getClass().equals(right.getClass()) && left instanceof Comparable) {
			int result;
			if (left instanceof Integer) {
				result = ((Comparable<Integer>) left).compareTo((Integer) right);
			} else if (left instanceof String) {
				result = ((Comparable<String>) left).compareTo((String) right);
			} else if (left instanceof Boolean) {
				result = ((Comparable<Boolean>) left).compareTo((Boolean) right);
			} else {
				throw new ODataApplicationException("Class " + left.getClass().getCanonicalName() + " not expected",
						HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
			}
			if (operator == BinaryOperatorKind.EQ) {
				return result == 0;
			} else if (operator == BinaryOperatorKind.NE) {
				return result != 0;
			} else if (operator == BinaryOperatorKind.GE) {
				return result >= 0;
			} else if (operator == BinaryOperatorKind.GT) {
				return result > 0;
			} else if (operator == BinaryOperatorKind.LE) {
				return result <= 0;
			} else {
				return result < 0;
			}

		} else {
			throw new ODataApplicationException("Comparision needs two equal types",
					HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
		}
	}

	private Object evaluateArithmeticOperation(BinaryOperatorKind operator, Object left, Object right)
			throws ODataApplicationException {

		if (left instanceof Integer && right instanceof Integer) {
			Integer valueLeft = (Integer) left;
			Integer valueRight = (Integer) right;

			if (operator == BinaryOperatorKind.ADD) {
				return valueLeft + valueRight;
			} else if (operator == BinaryOperatorKind.SUB) {
				return valueLeft - valueRight;
			} else if (operator == BinaryOperatorKind.MUL) {
				return valueLeft * valueRight;
			} else if (operator == BinaryOperatorKind.DIV) {
				return valueLeft / valueRight;
			} else {
				// BinaryOperatorKind,MOD
				return valueLeft % valueRight;
			}
		} else {
			throw new ODataApplicationException("Arithmetic operations needs two numeric operands",
					HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
		}
	}

	public Object visitMethodCall(MethodKind methodCall, List<Object> parameters)
			throws ODataApplicationException {
		if (methodCall == MethodKind.CONTAINS) {
			if (parameters.get(0) == null) {
				return false;
			}
			if (parameters.get(0) instanceof String && parameters.get(1) instanceof String) {
				String valueParam1 = (String) parameters.get(0);
				String valueParam2 = (String )parameters.get(1);

				return valueParam1.contains(valueParam2);
			} else {
				throw new ODataApplicationException("Contains needs two parametes of type Edm.String",
						HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
			}
		} else {
			throw new ODataApplicationException("Method call " + methodCall + " not implemented",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

	public Object visitTypeLiteral(EdmType type) throws ODataApplicationException {
		throw new ODataApplicationException("Type literals are not implemented",
				HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
	}

	public Object visitAlias(String aliasName) throws ODataApplicationException {
		throw new ODataApplicationException("Aliases are not implemented",
				HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
	}

	public Object visitEnum(EdmEnumType type, List<String> enumValues)
			throws ODataApplicationException {
		throw new ODataApplicationException("Enums are not implemented", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
				Locale.ENGLISH);
	}

	public Object visitLambdaExpression(String lambdaFunction, String lambdaVariable, Expression expression)
			throws ODataApplicationException {
		throw new ODataApplicationException("Lamdba expressions are not implemented",
				HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
	}

	public Object visitLambdaReference(String variableName) throws ODataApplicationException {
		throw new ODataApplicationException("Lamdba references are not implemented",
				HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
	}

	public Object visitMember(Member member) throws ODataApplicationException {
		final UriInfoResource uriInfoResource = member.getResourcePath();
		final List<UriResource> uriResourceParts = uriInfoResource.getUriResourceParts();
		if (uriResourceParts.size() == 1 && uriResourceParts.get(0) instanceof UriResourcePrimitiveProperty) {
			UriResourcePrimitiveProperty uriResourceProperty = (UriResourcePrimitiveProperty) uriResourceParts.get(0);
			if (currentEntity.getProperty(uriResourceProperty.getProperty().getName()) == null) {
				return null;
			}
			return currentEntity.getProperty(uriResourceProperty.getProperty().getName()).getValue();
		} else {
			throw new ODataApplicationException("Only primitive properties are implemented in filter expressions",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

	@Override
	public Object visitBinaryOperator(BinaryOperatorKind operator, Object left, List<Object> rights)
			throws ODataApplicationException {
			//FIXME: Needs to be implemented properly since olingo version 4.7.1, using first parameter as default
		Object right = rights.get(0);
		if (left == null) {
			return false;
		}
		if (operator == BinaryOperatorKind.ADD || operator == BinaryOperatorKind.MOD
				|| operator == BinaryOperatorKind.MUL || operator == BinaryOperatorKind.DIV
				|| operator == BinaryOperatorKind.SUB) {
			return evaluateArithmeticOperation(operator, left, right);
		} else if (operator == BinaryOperatorKind.EQ || operator == BinaryOperatorKind.NE
				|| operator == BinaryOperatorKind.GE || operator == BinaryOperatorKind.GT
				|| operator == BinaryOperatorKind.LE || operator == BinaryOperatorKind.LT) {
			return evaluateComparisonOperation(operator, left, right);
		} else if (operator == BinaryOperatorKind.AND || operator == BinaryOperatorKind.OR) {
			return evaluateBooleanOperation(operator, left, right);
		} else {
			throw new ODataApplicationException("Binary operation " + operator.name() + " is not implemented",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

}
