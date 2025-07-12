package io.github.linwancen.plugin.line;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ide.util.treeView.PresentableNodeDescriptor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packageDependencies.ui.PackageDependenciesNode;
import com.intellij.psi.PsiBinaryFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLargeFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import io.github.linwancen.plugin.line.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class TreeNum implements ProjectViewNodeDecorator {

    private static final Logger LOG = LoggerFactory.getLogger(TreeNum.class);

    @Override
    public void decorate(@NotNull ProjectViewNode node, @NotNull PresentationData data) {
        try {
            String num = num(node);
            if (num == null) {
                return;
            }
            addText(data, num);
        } catch (ProcessCanceledException e) {
            throw e;
        } catch (Throwable e) {
            LOG.info("TreeState catch Throwable but log to record.", e);
        }
    }

    static void addText(@NotNull PresentationData data, @Nullable String text) {
        if (text == null) {
            return;
        }
        @NotNull List<PresentableNodeDescriptor.ColoredFragment> coloredText = data.getColoredText();
        if (coloredText.isEmpty()) {
            data.addText(data.getPresentableText(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
        data.addText(" " + text, SimpleTextAttributes.GRAY_ATTRIBUTES);
    }

    private static String num(@NotNull ProjectViewNode<?> node) {
        @Nullable Project project = node.getProject();
        if (project == null) {
            return null;
        }
        if (DumbService.isDumb(project)) {
            return null;
        }
        Object value = node.getValue();
        if (!(value instanceof PsiElement)) {
            return childNode(node);
        }
        @NotNull PsiElement psiElement = (PsiElement) value;
        // Use it because in kotlin node.getVirtualFile() is null
        @Nullable PsiFile psiFile = psiElement.getContainingFile();
        if (psiFile == null || psiFile.isDirectory()) {
            return childNode(node);
        }
        // class/png/zip/... show file size
        if (psiFile instanceof PsiBinaryFile || psiFile instanceof PsiLargeFile) {
            long length = psiFile.getVirtualFile().getLength();
            return StringUtil.formatFileSize(length);
        }
        // method
        boolean parentIsDir = parentIsDir(node);
        if (!parentIsDir && AppSettingsState.getInstance().methodLine) {
            int line = LineNum.strLine(psiElement.getText());
            PsiComment @Nullable [] comments = PsiTreeUtil.getChildrenOfType(psiElement, PsiComment.class);
            if (comments != null) {
                for (@NotNull PsiComment comment : comments) {
                    int docLine = LineNum.strLine(comment.getText());
                    if (docLine > 0) {
                        line -= docLine;
                    }
                }
            }
            return String.valueOf(line);
        }
        // file
        if (!AppSettingsState.getInstance().fileLine) {
            return null;
        }
        @Nullable VirtualFile virtualFile = psiFile.getVirtualFile();
        if (virtualFile == null) {
            return null;
        }
        @NotNull String path = virtualFile.getPath();
        return String.valueOf(LineNum.fileLine(path));
    }

    private static String childNode(@NotNull ProjectViewNode<?> node) {
        if (!AppSettingsState.getInstance().childNum) {
            return null;
        }
        Collection<? extends AbstractTreeNode<?>> children;
        try {
            children = node.getChildren();
        } catch (Throwable ignored) {
            // java.lang.ArrayIndexOutOfBoundsException: Index ... out of bounds for length ...
            return null;
        }
        return String.valueOf(children.size());
    }

    private static boolean parentIsDir(@NotNull ProjectViewNode<?> node) {
        @Nullable AbstractTreeNode<?> parent = node.getParent();
        // PackageElementNode is java-impl.jar
        return parent instanceof PsiDirectoryNode;
    }

    @Override
    public void decorate(PackageDependenciesNode node, ColoredTreeCellRenderer cellRenderer) {
        // not need
    }
}
