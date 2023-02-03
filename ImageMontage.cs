using System;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;
using MetadataExtractor;

namespace ImageMontage
{
    public partial class MontageForm : Form
    {
        private Image[] images;

        public MontageForm()
        {
            InitializeComponent();

            var fileDialog = new OpenFileDialog();
            fileDialog.Multiselect = true;
            var result = fileDialog.ShowDialog();
            if (result == DialogResult.OK)
            {
                var files = fileDialog.FileNames;
                images = files
                    .Select(f => Image.FromFile(f))
                    .OrderBy(i =>
                    {
                        var metadata = ImageMetadataReader.ReadMetadata(f);
                        var exifDirectory = metadata.OfType<ExifIFD0Directory>().FirstOrDefault();
                        if (exifDirectory != null && exifDirectory.TryGetDateTime(ExifIFD0Directory.TagDateTime, out var dateTime))
                        {
                            return dateTime;
                        }
                        return File.GetLastWriteTime(f);
                    })
                    .ToArray();
            }
        }

        protected override void OnPaint(PaintEventArgs e)
        {
            base.OnPaint(e);

            int x = 0;
            int y = 0;
            int maxHeight = 0;
            int aspectRatioWidth = 8;
            int aspectRatioHeight = 10;

            foreach (var image in images)
            {
                if (x + image.Width > ClientSize.Width)
                {
                    x = 0;
                    y += maxHeight;
                    maxHeight = 0;
                }

                maxHeight = Math.Max(maxHeight, image.Height);
                e.Graphics.DrawImage(image, x, y);

                x += image.Width;
            }
        }
    }

    static class Program
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MontageForm());
        }
    }
}
